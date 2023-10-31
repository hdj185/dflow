package com.dflow.admin.approval.requestDto;

import com.dflow.entity.DocumentTypeFolder;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocTypeFolderReq {

    /** 폴더 명 **/
    private String typeFolderName;
    /** 부모 폴더 명 **/
    private Long parentFolder = 1L;
    /** 폴더 depth **/
    private int depth = 1;
    /** 폴더 명 **/
    private String typeFolderFlag = "Y";
    /** 폴더 정렬 순서 **/
    private Integer orderValue;

    public DocumentTypeFolder toEntity(){
        return DocumentTypeFolder.builder()
                .typeFolderName(this.typeFolderName)
                .parentFolderNo(this.parentFolder)
                .depth(this.depth)
                .typeFolderFlag(this.typeFolderFlag)
                .orderValue(this.orderValue)
                .build();
    }

}
