package com.dflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REFERENCE_APPROVAL", schema = "dflow")
public class Reference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REFERENCE_NO", nullable = false)
    private Long refNo;                                                 //양식폴더 번호

    @Column(name = "DOCUMENT_CHECK", nullable = false)
    private String docCheck;                                            //문서 확인 여부 (디폴트: "N")
    @Column(name = "DOCUMENT_NO", nullable = false)
    private Long docNo;                                                 //양식폴더 번호
    @Column(name = "REFERENCE_MEMBER_NO", nullable = false)
    private Long refMemberNo;                                           // 참조자 직원 번호


    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "REFERENCE_MEMBER_NO", referencedColumnName = "MEMBER_NO", insertable = false, updatable = false)
    private MemberInfo member;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCUMENT_NO", referencedColumnName = "DOCUMENT_NO", insertable = false, updatable = false)
    private DocumentApproval document;

}
