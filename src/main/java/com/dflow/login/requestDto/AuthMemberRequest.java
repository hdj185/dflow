package com.dflow.login.requestDto;

import com.dflow.entity.MemberInfo;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthMemberRequest {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(regexp = "^[a-z0-9_]{1,10}$", message = "아이디는 영어 소문자와 숫자, 언더바(_)만 사용하여 10자리여야 합니다.")
    private String memberId;        //사원 아이디

    @Pattern(regexp = "^[a-zA-Z\\d!@#$%^&*]{1,20}$", message = "비밀번호는 공백 제외, 영어 대소문자와 숫자, 특수문자를 포함한 20자 이하여야 합니다.")
    private String memberPw;        //사원 비밀번호

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String memberNameKr;        //사원 한글 이름

    private String memberNameEn;        //사원 영어 이름

    private String memberNameCn;        //사원 한문 이름

    @NotNull(message = "생년월일은 필수 입력값입니다.")
    @NotBlank(message = "연락처는 필수 입력값입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "연락처는 앞자리 '01'을 포함하여 10 ~ 11 자리로 입력 가능합니다.")
    private String memberPhone;        //사원 연락처
    private String memberBirthdate;        //사원 생년월일


//    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "올바른 이메일 주소 형식이 아닙니다.")
    private String memberEmail;        //사원 이메일

    private String memberAddress;           //사원 주소

    private Long departmentNo;              //부서

    private Long staffNo;                   //직책

    private String memberEnableDate;        //입사일자

    private String memberFlag;              //사용 유무

    private String memberLeaveDate;         //퇴사일자

    private Long updateNo;                  //수정자 번호

    @NotBlank(message = "성별은 필수 입력값입니다.")
    private String memberGender;            //사원 성별


    // 회원가입 틀에서 받아온 값을 DB에 저장하기
    public MemberInfo toEntity(PasswordEncoder passwordEncoder) {

        return MemberInfo.builder() // .엔티티필드명(this.DTO필드명)
                .memberId(this.memberId)
                .memberPw(passwordEncoder.encode(this.getMemberPw()))
                .memberNameKr(this.memberNameKr)
                .memberNameEn(this.memberNameEn)
                .memberNameCn(this.memberNameCn)
                .memberBirthdate(getDate(this.memberBirthdate))
                .memberPhone(this.memberPhone)
                .memberEmail(this.memberEmail)
                .memberAddress(this.memberAddress)
                .departmentNo(this.departmentNo)
                .staffNo(this.staffNo)
                .memberEnableDate(getDate(this.memberEnableDate))
                .memberLeaveDate(getDate(this.memberLeaveDate))
                .memberGender(this.memberGender)
                .memberRole("USER")
                .memberFlag(this.memberFlag)
                .updateNo(this.updateNo)
                .build();
    }

    public LocalDate getMemberLeaveDate() {
        return getDate(this.memberLeaveDate);
    }

    public LocalDate getDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date == null || "".equals(date) ? null : LocalDate.parse(date, formatter);
    }

    public boolean getIsRetired() {

        return this.getMemberLeaveDate() != null;
    }
}
