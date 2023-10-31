package com.dflow.login.responseDto;

import com.dflow.entity.MemberInfo;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespMdfMember {

    private String memberId;        //사원 아이디

    private String memberPw;        //사원 비밀번호

    private String memberNameKr;        //사원 한글 이름

    private String memberNameEn;        //사원 영어 이름

    private String memberNameCn;        //사원 한문 이름

    private LocalDate memberBirthdate;        //사원 생년월일

    private String memberPhone;        //사원 연락처

    private String memberEmail;        //사원 이메일

    private String memberAddress;        //사원 주소

    private Long departmentNo;      //부서

    private Long staffNo;            //직책

    private LocalDate memberEnableDate;        //입사일자

    private LocalDate memberLeaveDate;        //퇴사일자

    private String memberGender;        //사원 성별
    private Long memberImgNo;     //이미지 파일 고유번호


    public static RespMdfMember of(MemberInfo memberInfo) {
        return RespMdfMember.builder()
                .memberId(memberInfo.getMemberId())
                .memberPw(memberInfo.getMemberPw())
                .memberNameKr(memberInfo.getMemberNameKr())
                .memberNameEn(memberInfo.getMemberNameEn())
                .memberNameCn(memberInfo.getMemberNameCn())
                .memberBirthdate(memberInfo.getMemberBirthdate())
                .memberPhone(memberInfo.getMemberPhone())
                .memberEmail(memberInfo.getMemberEmail())
                .memberAddress(memberInfo.getMemberAddress())
                .departmentNo(memberInfo.getDepartmentNo())
                .staffNo(memberInfo.getStaffNo())
                .memberEnableDate(memberInfo.getMemberEnableDate())
                .memberLeaveDate(memberInfo.getMemberLeaveDate())
                .memberGender(memberInfo.getMemberGender())
                .memberImgNo(memberInfo.getImgAttachNo())
                .build();
    }

}
