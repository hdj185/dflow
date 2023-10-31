package com.dflow.admin.system.responseDto;

import com.dflow.entity.MemberInfo;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@ToString
public class AuthoSelResp {

    /** 멤버 고유 번호 **/
    private Long memberNo;

    /** 멤버 아이디 **/
    private String memberId;

    /** 멤버 한글 이름 **/
    private String memberNameKr;

    /** 멤버의 직책 고유 번호 **/
    private Long staffNo;

    /** 멤버의 직책 이름 **/
    private String staffName;

    /** 멤버 활성화 여부  **/
    private String memberFlag;

    /**멤버 권한 **/
    private String memberRole;

    public static AuthoSelResp toDto(MemberInfo memberInfo){
        return AuthoSelResp.builder()
                .memberNo(memberInfo.getMemberNo())
                .memberId(memberInfo.getMemberId())
                .memberNameKr(memberInfo.getMemberNameKr())
                .staffNo(memberInfo.getStaff().getStaffNo())
                .staffName(memberInfo.getStaff().getStaffName())
                .memberFlag(memberInfo.getMemberFlag())
                .memberRole(memberInfo.getMemberRole())
                .build();
    }
}
