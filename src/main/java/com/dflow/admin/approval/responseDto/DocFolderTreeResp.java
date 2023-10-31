package com.dflow.admin.approval.responseDto;


import com.dflow.entity.Department;
import com.dflow.entity.DocumentType;
import com.dflow.entity.DocumentTypeFolder;
import com.dflow.organization.responseDto.DeptTreeResp;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
@ToString
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class DocFolderTreeResp {

    /** 타입 폴더 고유번호 **/
    private Long typeFolderNo;

    /** 타입 폴더 이름  **/
    private String typeFolderName;

    /** 계층 **/
    private int depth;

    /** 사용 여부 **/
    private String typeFolderFlag;

    private Integer orderValue;

    /** 하위 부서 리스트 **/
    private List<DocFolderTreeResp> children;

    private List<DocTypeInfoResp> typeList;


    // 도큐먼트폴더 번호, 이름, 계층번호, 자식 개체를 가져와서 dto로 변환
    public static DocFolderTreeResp toDto(DocumentTypeFolder documentTypeFolder){
        return new DocFolderTreeResp(
                documentTypeFolder.getTypeFolderNo(),
                documentTypeFolder.getTypeFolderName(),
                documentTypeFolder.getDepth(),
                documentTypeFolder.getTypeFolderFlag(),
                documentTypeFolder.getOrderValue(),
                documentTypeFolder.getChildrenFolder()
                        .stream()
                        .map(DocFolderTreeResp::toDto)
                        .filter(dto -> "Y".equals(dto.getTypeFolderFlag()))
                        .collect(Collectors.toList()),
                documentTypeFolder.getTypeList().stream().map(DocTypeInfoResp::toDto).collect(Collectors.toList())
        );
    }

}
