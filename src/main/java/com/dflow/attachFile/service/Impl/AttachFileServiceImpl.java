package com.dflow.attachFile.service.Impl;

import com.dflow.admin.approval.requestDto.AdminDocTypeInsReq;
import com.dflow.admin.approval.requestDto.AdminDocTypeUdtReq;
import com.dflow.attachFile.requestDto.ReqFile;
import com.dflow.attachFile.service.AttachFileService;
import com.dflow.entity.AttachFile;
import com.dflow.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.util.UriUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttachFileServiceImpl implements AttachFileService {

    private final AttachFileRepository attachFileRepository;
    @Value("${file.uploadDir}")
    private String uploadPath;

    //파일 업로드
    //매개변수 - MultipartFile file: 파일
    //반환값 - 기본: 저장된 파일 정보, 그외: null
    public AttachFile insFile(MultipartFile file) {
        if(file.isEmpty()) return null;

        try {
            //DB에 들어갈 파일 정보 request로 생성
            ReqFile reqFile = new ReqFile(file, uploadPath);

            //실제 파일 저장
            file.transferTo(new File(reqFile.getFileLocation()));

            //데이터베이스에 파일 정보 저장
            AttachFile attachFile = reqFile.toEntity();
            return attachFileRepository.save(attachFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //단일 (이미지) 파일 불러오기
    //매개변수 - Long fileNo: 파일 엔티티의 고유번호
    @Override
    public Resource selFile(Long fileNo) {
        try {
            Optional<AttachFile> optionalAttachFile = attachFileRepository.findById(fileNo);
            if(optionalAttachFile.isEmpty()) return null;   //파일이 없으면 null

            AttachFile attachFile = optionalAttachFile.get();
            return new UrlResource("file:"+attachFile.getFileLocation());
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResponseEntity<Resource> downloadBoardFile(Long fileNo) {
        try {
            Optional<AttachFile> optionalAttachFile = attachFileRepository.findById(fileNo);
            if(optionalAttachFile.isEmpty()) return null;   //파일이 없으면 null

            AttachFile attachFile = optionalAttachFile.get();
            UrlResource resource = new UrlResource("file:" + attachFile.getFileLocation());

            String originalFileName = attachFile.getFileOriginName();
            String encodedOriginalFileName = UriUtils.encode(originalFileName, StandardCharsets.UTF_8);
            String contentDisposition = "attachment; filename=\"" + encodedOriginalFileName + "\"";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AttachFile insHtmlFile(AdminDocTypeInsReq docTypeInsReq) {
        if(docTypeInsReq == null) return null;

        System.out.println("content=" + docTypeInsReq.getContents());
        //문자열 byte[]로 변환
        byte[] contentBytes = docTypeInsReq.getContents().getBytes(StandardCharsets.UTF_8);

        // MultipartFile 객체 생성
        String fileName = docTypeInsReq.getDocFormName() + ".html";
        MultipartFile multipartFile = new MockMultipartFile("file", fileName, "text/html", contentBytes);

        return insFile(multipartFile);
    }

    @Override
    public AttachFile updHtmlFile(AdminDocTypeUdtReq docTypeUdtReq) {
        if(docTypeUdtReq == null) return null;

        System.out.println("content=" + docTypeUdtReq.getContents());
        //문자열 byte[]로 변환
        byte[] contentBytes = docTypeUdtReq.getContents().getBytes(StandardCharsets.UTF_8);

        // MultipartFile 객체 생성
        String fileName = docTypeUdtReq.getDocFormName() + ".html";
        MultipartFile multipartFile = new MockMultipartFile("file", fileName, "text/html", contentBytes);

        return insFile(multipartFile);
    }

    //html파일을 String형으로 반환
    @Override
    public String readHtmlFileToString(Long attachFileNo) {

        AttachFile attachFile = attachFileRepository.findById(attachFileNo).orElseThrow();
        String path = attachFile.getFileLocation();
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    //파일 고유번호(fileAttachNo)로 엔티티 찾기
    //db에 파일 정보가 있으면 파일 엔티티 반환, 없으면 null 반환
    @Override
    public AttachFile selAttachFile(Long fileAttachNo) {
        Optional<AttachFile> optional = attachFileRepository.findById(fileAttachNo);
        if(optional.isPresent()) return optional.get();
        else return null;
    }


    @Override
    public MultipartFile cropFile(MultipartFile signImg) {
        MultipartFile croppedMultipartFile = null;

        BufferedImage orginSignImg = null;
        BufferedImage cropSignImg = null;
        BufferedImage destImg = null;
        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;

        String oriname = signImg.getOriginalFilename();
        String fileExt = oriname.substring(oriname.lastIndexOf("."));

        try {
            orginSignImg = ImageIO.read(signImg.getInputStream());

            // 저장할 서명의 너비와 높이
            int dw = 170, dh = 170;

            // 원본 이미지의 너비와 높이
            int ow = orginSignImg.getWidth();
            int oh = orginSignImg.getHeight();

            // 원본 너비를 기준으로 하여 저장할 서명의 비율로 높이 계산
            int nw = ow;
            int nh = (ow * dh) / dw;

            // 계산된 높이가 원본보다 높다면 crop이 안되므로
            // 원본 높이를 기준으로 썸네일의 비율로 너비 계산
            if (nh > oh) {
                nw = (oh * dw) / dh;
                nh = oh;
            }

            // 계산된 크기로 원본 이미지를 가운데에서 crop
            cropSignImg = Scalr.crop(orginSignImg, (ow - nw) / 2, (oh - nh) / 2, nw, nh);

            // crop된 이미지로 썸네일 생성
            destImg = Scalr.resize(cropSignImg, dw, dh);

//             BufferedImage를 byte 배열로 변환
            outputStream = new ByteArrayOutputStream();



            ImageIO.write(destImg, fileExt, outputStream);

            // byte 배열로부터 ByteArrayInputStream 생성
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            // ByteArrayInputStream을 MockMultipartFile로 변환
            croppedMultipartFile = new MockMultipartFile(
                    oriname,      // 파일 이름 (원하는 이름으로 수정)
                    oriname,      // 원본 파일 이름 (원하는 이름으로 수정)
                    signImg.getContentType(),                // 이미지 타입 (실제 이미지 타입에 맞게 수정)
                    inputStream  // 이미지 데이터 (ByteArrayInputStream을 사용하여 변환한 데이터)
            );
//            croppedMultipartFile = new MockMultipartFile(oriname.substring(0, oriname.lastIndexOf(".")), inputStream.readAllBytes());
            inputStream.close();
            outputStream.close();
            orginSignImg.flush();
            cropSignImg.flush();
            destImg.flush();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return croppedMultipartFile;
    }
}

