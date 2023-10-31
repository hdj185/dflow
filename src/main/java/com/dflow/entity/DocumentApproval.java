package com.dflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "document_approval", schema = "dflow")
@Getter   // 23-8-24 추가 확인 후 삭제
public class DocumentApproval extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DOCUMENT_NO", nullable = false)
    private Long docNo;                                             //문서 번호

    @Column(name = "DOCUMENT_TTL", nullable = false)
    private String docTTL;                                          //문서 제목
    @Column(name = "DOCUMENT_CONTENT", nullable = false, columnDefinition = "TEXT")
    private String docCn;                                           //문서 내용
    @Column(name = "DOCUMENT_STATE", nullable = false)
    private String docState;                                        //문서 상태(기본 대기)
    @Column(name = "DOCUMENT_RECOVERY", nullable = false)
    private String docRecovery;                                     //문서 회수(기본 "N" / 회수 시 "Y")
    @Column(name = "DOCUMENT_CHECK", nullable = false)
    private String docFlag;                                        //문서 체크(사용 여부)
    @Column(name = "FILE_ATTACH_NO")
    private Long fileAttachNo;                                      //첨부 파일

    @Column(name = "DOCUMENT_FORM_NO", nullable = false)
    private Long docFormNo;                                         // 문서양식번호


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATE_NO", referencedColumnName = "MEMBER_NO", insertable = false, updatable = false)
    private MemberInfo memberInfo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATE_NO", referencedColumnName = "MEMBER_NO", insertable = false, updatable = false)
    private MemberInfo updateMember;

    @OneToMany(mappedBy = "document", fetch = FetchType.LAZY)
    private List<Approval> approvalList;


    // 참조
    @OneToMany(mappedBy = "document", fetch = FetchType.LAZY)
    private List<Reference> referenceList;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCUMENT_FORM_NO", insertable = false, updatable = false )
    private DocumentType documentTypeInfo;


    public void setDocState(String docState) {
        this.docState = docState;
    }
    public void changeDocTTL(String ttl){ this.docTTL = docTTL;}
    public void changeDocCn(String docCn){ this.docCn = docCn;}
    public void changeDocFormNo(Long docFormNo){this.docFormNo = docFormNo;}

    public void setDocCn(String docCn) {
        this.docCn = docCn;
    }

    public void setDocFlag(String docFlag) { this.docFlag = docFlag;}
}
