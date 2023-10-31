package com.dflow.attachFile.service;

import com.dflow.admin.approval.requestDto.AdminDocTypeInsReq;
import com.dflow.admin.approval.requestDto.AdminDocTypeUdtReq;
import com.dflow.entity.AttachFile;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AttachFileService {
    //파일 업로드 (단일)
    AttachFile insFile(MultipartFile file);

    //이미지 출력 (단일)
    Resource selFile(Long fileNo);
    //파일 다운로드 (단일)
    ResponseEntity<Resource> downloadBoardFile(Long fileNo);
    //문자열 html파일로 변환하여 다운로드
    AttachFile insHtmlFile(AdminDocTypeInsReq docTypeInsReq);
    AttachFile updHtmlFile(AdminDocTypeUdtReq docTypeUdtReq);
    //html 파일을 문자열로 변환하여 반환
    String readHtmlFileToString(Long attachFileNo);
    //파일 고유번호(fileAttachNo)로 엔티티 찾기
    AttachFile selAttachFile(Long fileAttachNo);
    //크롭 이미지 추출
    MultipartFile cropFile(MultipartFile signImg);
}
