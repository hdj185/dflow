package com.dflow.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "document_type", schema = "dflow")
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DOCUMENT_FORM_NO", nullable = false)
    private Long docFormTypeNo;     //문서양식번호

    @Column(name = "DOCUMENT_FORM_NAME", nullable = false)
    private String docFormName;    //문서양식명
    @Column(name = "DOCUMENT_FORM_CODE", nullable = false)
    private String docFormCode;    //문서양식코드
    @Column(name = "DOCUMENT_FORM_USE", nullable = false)
    private String docFormUseFlag;    // 사용여부
    @Column(name = "FOLDER_NO", nullable = false)
    private Long folderNo;    // 파일 상위폴더 번호
    @Column(name = "FILE_ATTACH_NO")
    private Long fileAttachNo;    // 첨부파일 번호
    @Column(name = "ORDER_VALUE")
    private Integer orderValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLDER_NO", referencedColumnName = "TYPE_FOLDER_NO" , insertable = false, updatable = false)
    private DocumentTypeFolder typeFolder;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_ATTACH_NO",  insertable = false, updatable = false)
    private AttachFile attachFile;
}


