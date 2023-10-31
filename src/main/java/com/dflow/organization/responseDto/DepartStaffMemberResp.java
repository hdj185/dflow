package com.dflow.organization.responseDto;

import com.dflow.entity.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

/**
 *     전체 조직도
 * **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartStaffMemberResp {

    /** 멤버고유 번호 **/
    private Long memberNo;

    /** 스태프 고유 번호 **/
    private Long staffNo;

    /** 멤버 한글 이름 **/
    private String memberNameKr;

    /**  부서명 **/
    private String departmentName;

    /** 전화번호 **/
    private String memberPhone;

    /** 직책명 **/
    private String staffName;

    /** 멤버 이메일 **/
    private String memberEmail;

    /** 맴버 사진 첨부 파일 **/
    private Long imgNo;


    /** 전체 조직도에 띄울 내용을 Dto로 바꾸는 부분 **/
    public static DepartStaffMemberResp toDto(MemberInfo memberInfo){
        return DepartStaffMemberResp.builder()
                .memberNo(memberInfo.getMemberNo())
                .memberNameKr(memberInfo.getMemberNameKr())
                .memberPhone(memberInfo.getMemberPhone())
                .departmentName(memberInfo.getDepartment().getDepartmentName())
                .staffName(memberInfo.getStaff().getStaffName())
                .imgNo(memberInfo.getImgAttachNo())
                .memberEmail(memberInfo.getMemberEmail())
                .staffNo(memberInfo.getStaff().getStaffNo())
                .build();
    }
    

}
