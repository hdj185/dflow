package com.dflow.admin.approval.requestDto;

import lombok.Data;

@Data
public class UdtFolderReq {

    /** 폴더 고유 번호 **/
    private Long folderNo;
    /** 폴더 명 **/
    private String folderName;
}
