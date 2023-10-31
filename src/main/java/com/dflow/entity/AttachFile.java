package com.dflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "attach_file", schema = "dflow")
@Getter
@Builder
@AllArgsConstructor
public class AttachFile extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ATTACH_NO", nullable = false)
    private Long fileAttachNo;     // 첨부파일 고유번호
    
    @Column(name = "FILE_ORIGIN_NAME", nullable = false)
    private String fileOriginName;     //기존 파일 명
    
    @Column(name = "FILE_SAVE_NAME", nullable = false)
    private String fileSaveName;     //저장 파일 명
    
    @Column(name = "FILE_EXT", nullable = false)
    private String fileExt;     //파일 확장자
    
    @Column(name = "FILE_SIZE", nullable = false)
    private Long fileSize;     //파일 크기
    
    @Column(name = "FILE_LOCATION", nullable = false)
    private String fileLocation;     //파일 경로
    
    @Column(name = "FILE_FLAG", nullable = false, columnDefinition = "bit(1) default 0")
    private Boolean fileFlag;     //파일 상태

    public void changeFileFlag(Boolean fileFlag){this.fileFlag = fileFlag;}

}
