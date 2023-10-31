package com.dflow.attachFile.requestDto;

import com.dflow.entity.AttachFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqFile {
    private String fileOriginName;      //기존 파일 명
    private String fileSaveName;        //저장 파일 명
    private String fileExt;             //파일 확장자
    private Long fileSize;              //파일 크기
    private String fileLocation;        //파일 경로

    public ReqFile(MultipartFile file, String uploadPath) {
        // 원래 파일 이름 추출
        setFileOriginName(file.getOriginalFilename());

        // 확장자 추출(ex : .png)
        setFileExt(getFileOriginName().substring(getFileOriginName().lastIndexOf(".")));

        // 파일 이름으로 쓸 uuid 생성
        setFileSaveName(UUID.randomUUID().toString() + getFileExt());

        // 파일을 불러올 때 사용할 파일 경로
        setFileLocation(uploadPath + getFileSaveName());

        // 파일 사이즈
        setFileSize(file.getSize());
    }

    //dto -> entity 변환
    public AttachFile toEntity() {
        return AttachFile.builder()
                .fileOriginName(this.getFileOriginName())
                .fileSaveName(this.getFileSaveName())
                .fileExt(this.getFileExt())
                .fileSize(this.getFileSize())
                .fileLocation(this.getFileLocation())
                .fileFlag(false)
                .build();
    }
}
