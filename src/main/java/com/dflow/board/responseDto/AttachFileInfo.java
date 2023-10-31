package com.dflow.board.responseDto;

import com.dflow.entity.AttachFile;
import com.dflow.entity.ReferenceFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttachFileInfo {

    private Long fileAttachNo;
    private String fileOriginName;
    private String fileLocation;
    private Long referenceFileNo;
    private Long fileSize;

    public static AttachFileInfo toDto( ReferenceFile file){
        return AttachFileInfo.builder()
                .fileAttachNo(file.getAttachFile().getFileAttachNo())
                .referenceFileNo(file.getReferenceFileNO())
                .fileOriginName(file.getAttachFile().getFileOriginName())
                .fileLocation(file.getAttachFile().getFileLocation())
                .fileSize(file.getAttachFile().getFileSize())
                .build();
    }
}
