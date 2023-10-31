package com.dflow.approval.requestDto;

import com.dflow.entity.Approval;
import com.dflow.entity.DocumentApproval;
import com.dflow.entity.MemberInfo;
import com.dflow.entity.Reference;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocAprvInsReq {
    private String docTTL;                                              //문서 제목
    private String docCn;                                               //문서 내용
    //TODO: 현재 첨부파일 기능 없음. null로 들어감
    private Long docFormNo;                                             //문서양식번호
    private MemberInfo memberInfo;                                      //기안자
    private List<Long> approver;                                        //결재자 고유번호 리스트
    private List<Long> referrer;                                        //참조자 고유번호 리스트


    // 문서 엔티티(DocumentApproval) 생성
    public DocumentApproval toDocEntity() {
        return DocumentApproval.builder()
                .docTTL(this.docTTL)
                .docCn(this.docCn)
                .docState("대기")
                .docRecovery("N")
                .docFlag("Y")
                .fileAttachNo(null)
                .docFormNo(this.docFormNo)
                .memberInfo(this.memberInfo)
                .build();
    }

    // 결재 엔티티(Approval) 생성
    public Approval toAprvEntity(Long documentNo, int orderNo, Long memberNo) {
        return Approval.builder()
                .aprvOrder(orderNo)
                .aprvResult(orderNo == 1 ? "진행중" : "-")
                .aprvCheck(orderNo == 1 ? "Y" : "N")
                .docNo(documentNo)
                .aprvMemberNo(memberNo)
                .build();
    }

    // 결재 엔티티(Approval) list 생성
    public List<Approval> toAprvEntity(Long documentNo) {
        List<Approval> list = new ArrayList<>();

        //결재자 목록
        for(int i = 0; i < approver.size(); i++)
            list.add(toAprvEntity(documentNo, i + 1, approver.get(i)));

        return list;
    }

    // 참조자 엔티티(Reference) 생성
    public Reference toRefEntity(Long documentNo, Long memberNo) {
        return Reference.builder()
                .docCheck("N")
                .docNo(documentNo)
                .refMemberNo(memberNo)
                .build();
    }

    // 참조자 엔티티(Reference) list 생성
    public List<Reference> toRefEntity(Long documentNo) {
        List<Reference> list = new ArrayList<>();
        for(Long memberNo : referrer)
            list.add(toRefEntity(documentNo, memberNo));
        return list;
    }
}
