package com.dflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "approval", schema = "dflow")
public class Approval extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPROVAL_NO", nullable = false)
    private Long aprvNo;                                                //결재 번호
    @Column(name = "APPROVAL_ORDER", nullable = false)
    private int aprvOrder;                                              //결재 순서
    @Column(name = "APPROVAL_OPINION")
    private String aprvOpinion;                                         //결재의견
    @Column(name = "APPROVAL_TIME")
    private LocalDateTime aprvTime;                                     //결재일
    @Column(name = "APPROVAL_RESULT", nullable = false)
    private String aprvResult;                                          //결재 결과
    @Column(name = "APPROVAL_CHECK", nullable = false)
    private String aprvCheck;                                           //결재 체크 여부

    @Column(name = "DOCUMENT_NO", nullable = false)
    private Long docNo;                                                 //결재 문서 번호

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCUMENT_NO", referencedColumnName = "DOCUMENT_NO", insertable = false, updatable = false)
    private DocumentApproval document;

    @Column(name = "APPROVAL_MEMBER_NO", nullable = false)
    private Long aprvMemberNo;                                              // 결재자 번호

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "APPROVAL_MEMBER_NO", referencedColumnName = "MEMBER_NO", insertable = false, updatable = false)
    private MemberInfo memberInfo;

//    @NotFound(action = NotFoundAction.IGNORE)
//    @ManyToOne(optional = true, fetch = FetchType.LAZY)
//    @JoinColumn(name = "DOCUMENT_NO", referencedColumnName = "DOCUMENT_NO", insertable = false, updatable = false)
//    private DocumentApproval documentApproval;


    public void setAprvCheck(String aprvCheck) {
        this.aprvCheck = aprvCheck;
    }

    public void setAprvOpinion(String aprvOpinion) {
        this.aprvOpinion = aprvOpinion;
    }

    public void setAprvTime(LocalDateTime aprvTime) {
        this.aprvTime = aprvTime;
    }

    public void setAprvResult(String aprvResult) {
        this.aprvResult = aprvResult;
    }
}
