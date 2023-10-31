package com.dflow.admin.approval.responseDto;

import com.dflow.entity.CodeInfo;
import com.dflow.entity.DocumentTypeFolder;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AprvFolderResp {

    private Long typeFolderNo;

    private String typeFolderName;

    private Integer orderValue;

    //entity -> dto
    public static AprvFolderResp of(DocumentTypeFolder documentTypeFolder) {
        return AprvFolderResp.builder()
                .typeFolderNo(documentTypeFolder.getTypeFolderNo())
                .typeFolderName(documentTypeFolder.getTypeFolderName())
                .orderValue(documentTypeFolder.getOrderValue())
                .build();
    }

    public static List<AprvFolderResp> of(List<DocumentTypeFolder> entityList) {
        List<AprvFolderResp> list = new ArrayList<>();

        entityList.sort(
                Comparator.comparing(DocumentTypeFolder::getOrderValue)
        );

        for(DocumentTypeFolder folder : entityList)

            list.add(of(folder));
        return list;
    }
}
