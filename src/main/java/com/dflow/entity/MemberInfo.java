package com.dflow.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "MEMBER_INFO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfo extends BaseTimeEntity {

    @Id
    @Column(name = "MEMBER_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @Column(name = "MEMBER_ID", nullable = false, unique = true)
    private String memberId;                                            //사원 아이디

    @Column(name = "MEMBER_PW", nullable = false)
    private String memberPw;                                            //사원 비밀번호

    @Column(name = "MEMBER_NAME_KR", nullable = false)
    private String memberNameKr;                                        //사원 한글 이름

    @Column(name = "MEMBER_NAME_EN")
    private String memberNameEn;                                        //사원 영어 이름

    @Column(name = "MEMBER_NAME_CN")
    private String memberNameCn;                                        //사원 한문 이름

    @Column(name = "MEMBER_BIRTHDATE", nullable = false)
    private LocalDate memberBirthdate;                                  //사원 생년월일

    @Column(name = "MEMBER_PHONE", nullable = false)
    private String memberPhone;                                         //사원 연락처

    @Column(name = "MEMBER_EMAIL")
    private String memberEmail;                                         //사원 이메일

    @Column(name = "MEMBER_ADDRESS")
    private String memberAddress;                                       //사원 주소

    @Column(name = "MEMBER_ENABLE_DATE", nullable = false)
    private LocalDate memberEnableDate;                                 //입사일자

    @Column(name = "MEMBER_LEAVE_DATE")
    private LocalDate memberLeaveDate;                                  //퇴사일자

    @Column(name = "MEMBER_GENDER", nullable = false)
    private String memberGender;                                        //사원 성별

    @Column(name = "DEPARTMENT_NO", nullable = false)
    private Long departmentNo;

    @Column(name = "STAFF_NO", nullable = false)
    private Long staffNo;

    @Column(name = "IMG_ATTACH_NO", nullable = true)
    private Long imgAttachNo;                                           //사원 사진

    @Column(name = "SIGN_ATTACH_NO", nullable = true)
    private Long signAttachNo;                                          //사원 서명

    @Column(name="MEMBER_FLAG")
    private String memberFlag;                                          //사용여부

    @Column(name = "UPDATE_NO")
    private Long updateNo;                                              // 수정자 No

    @Column(name = "MEMBER_ROLE", columnDefinition = "VARCHAR(255) DEFAULT 'USER'")
    private String memberRole;                                          // 권한


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_NO", insertable = false, updatable = false)
    private Department department;                                      //부서

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATE_NO",referencedColumnName = "MEMBER_NO", insertable = false, updatable = false)
    private MemberInfo updateInfo;                                      //수정자 정보

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "STAFF_NO", insertable = false, updatable = false)
    private Staff staff;                                                //직책

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMG_ATTACH_NO",referencedColumnName = "FILE_ATTACH_NO" , insertable = false, updatable = false)
    private AttachFile imgAttach;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SIGN_ATTACH_NO",referencedColumnName = "FILE_ATTACH_NO" , insertable = false, updatable = false)
    private AttachFile signAttach;


    public void changeMemberId(String memberId) {this.memberId = memberId;}
    public void changeMemberPw(String memberPw) {this.memberPw = memberPw;}
    public void changeMemberNameKr(String memberNameKr) {this.memberNameKr = memberNameKr;}
    public void changeMemberNameEn(String memberNameEn) {this.memberNameEn = memberNameEn;}
    public void changeMemberNameCn(String memberNameCn) {this.memberNameCn = memberNameCn;}
    public void changeMemberBirthdate(LocalDate memberBirthdate) {this.memberBirthdate = memberBirthdate;}
    public void changeMemberPhone(String memberPhone) {this.memberPhone = memberPhone;}
    public void changeMemberEmail(String memberEmail) {this.memberEmail = memberEmail;}
    public void changeMemberAddress(String memberAddress) {this.memberAddress = memberAddress;}
    public void changeMemberEnableDate(LocalDate memberEnableDate) {this.memberEnableDate = memberEnableDate;}
    public void changeMemberLeaveDate(LocalDate memberLeaveDate) {this.memberLeaveDate = memberLeaveDate;}
    public void changeMemberGender(String memberGender) {this.memberGender = memberGender;}
    public void changeDepartmentNo(Long departmentNo) {this.departmentNo = departmentNo;}
    public void changeStaffNo(Long staffNo) {this.staffNo = staffNo;}
    public void changeMemberRole(String memberRole) {this.memberRole = memberRole;}
    public void changeImgAttachNo(Long imgAttachNo) {this.imgAttachNo = imgAttachNo;}
    public void changeSignAttachNo(Long signAttachNo) {this.signAttachNo = signAttachNo;}
    public void changeMemberFlag(String memberFlag) {this.memberFlag = memberFlag;}
    public void changeUpdateNo(Long updateNo) {this.updateNo = updateNo;}

}
