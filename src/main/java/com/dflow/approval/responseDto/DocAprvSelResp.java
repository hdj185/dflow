package com.dflow.approval.responseDto;


import com.dflow.entity.DocumentApproval;
import com.dflow.entity.DocumentType;
import com.dflow.entity.ProjectInfo;
import com.dflow.login.CustomUser;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocAprvSelResp {

    private Long docNo;                                             //문서 번호

    private String docTTL;                                          //문서 제목     // 문서 번호

    private String docCn;                                           //문서 내용

    private String docState;                                        //문서 상태     // 진행상태

    private String codeName;

    private String docRecovery;                                     //문서 회수 // 확인 필요

    private String docFlag;                                        //문서 체크  // entity 명 document_check

    private Long fileAttachNo;                                      //첨부 파일

    private String createDate;                                          // 기안일 date

    private String updateDate;                                          // 결재일 date

    private String docFormNo;                                         // 문서양식번호

    private Long memberNo;                                          // 직원 번호

    private String memberNameKr;                                    // 기안자

    private String staffName;                                       // 직책 명

    private String departmentName;                                  // 부서 명
    private Long drafterSignNo;     //기안자 서명 번호
    private String memberId;

    private List<Long> fileNo;  //파일 no


    /**
     * 전체 문서 조회용
     **/
    public static DocAprvSelResp toDto(DocumentApproval documentApproval) {

        return DocAprvSelResp.builder()
                .docNo(documentApproval.getDocNo())
                .docFormNo(documentApproval.getDocumentTypeInfo().getDocFormName())
                .docTTL(documentApproval.getDocTTL())
                .docCn(documentApproval.getDocCn())
                .docState(documentApproval.getDocState())
                .docRecovery(documentApproval.getDocRecovery())
                .docFlag(documentApproval.getDocFlag())
                .createDate(dateToString(documentApproval.getCreateDate()))  // 기안일
                .updateDate(documentApproval.getDocState().equals("완료") || documentApproval.getDocState().equals("반려") ? dateToString(documentApproval.getUpdateDate()) : "")  // 결재일 완료거나 반려일 때만 표시
                .memberNo(documentApproval.getCreateNo())
                .memberNameKr(documentApproval.getMemberInfo().getMemberNameKr())
                .memberId(documentApproval.getMemberInfo().getMemberId())
                .staffName(documentApproval.getMemberInfo().getStaff().getStaffName())
                .drafterSignNo(documentApproval.getMemberInfo().getSignAttachNo())
                .departmentName(documentApproval.getMemberInfo().getDepartment().getDepartmentName())
                .build();
    }

    private static String dateToString(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd   HH:mm"));
    }

    //Page -> toDto
    public static Page<DocAprvSelResp> toDto(Page<DocumentApproval> entityList) {
        List<DocAprvSelResp> list = entityList.getContent().stream()
                .map(DocAprvSelResp::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(list, entityList.getPageable(), entityList.getTotalElements());
    }


}

