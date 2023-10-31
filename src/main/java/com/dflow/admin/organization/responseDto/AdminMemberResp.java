package com.dflow.admin.organization.responseDto;


import com.dflow.entity.MemberInfo;
import com.dflow.entity.Staff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** 어드민 임직원 관리 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminMemberResp {

    /** 멤버 고유 번호 **/
    private Long memberNo;

    /** 멤버 부서 고유 번호 **/
    private Long departmentNo;

    /** 멤버 부서의 상위 부서의 고유 번호 **/
    private Long departmentParentNo;

    /** 멤버 한글 이름 **/
    private String memberNameKr;

    /** 멤버의 부서 이름 **/
    private String departmentName;

    /** 멤버의 직책 고유 번호 **/
    private Long staffNo;

    /** 멤버의 직책 이름 **/
    private String staffName;

    /** 임직원 정보 수정한 사람의 이름   **/
    private String updateName;

    /** 임직원 정보 수정한 사람의 번호  **/
    private Long updateNo;

    /** 임직원 정보를 수정한 날짜  **/
    private String updateDate;

    /** 멤버 활성화 여부  **/
    private String memberFlag;

    /** 멤버 퇴사 일 **/
    private LocalDate memberLeaveDate;

    /**멤버 입사일.. null = false 라 입력해줘야 함..**/
    private LocalDate memberEnableDate;


    /** 23-8-18**/
    public static AdminMemberResp toDto(MemberInfo memberInfo){
        return AdminMemberResp.builder()
                .memberNo(memberInfo.getMemberNo())
                .departmentNo(memberInfo.getDepartment().getDepartmentNo())
                .departmentParentNo(memberInfo.getDepartment().getDepartmentParentNo() != null ? memberInfo.getDepartment().getDepartmentParentNo().getDepartmentNo() : null)
                .staffNo(memberInfo.getStaff().getStaffNo())
                .departmentName(memberInfo.getDepartment().getDepartmentName())
                .memberNameKr(memberInfo.getMemberNameKr())
                .staffName(memberInfo.getStaff().getStaffName())
                .updateDate(dateToString(memberInfo.getUpdateDate()))
                .memberFlag(memberInfo.getMemberFlag() == null || memberInfo.getMemberFlag().equals("0") ? "0" : memberInfo.getMemberFlag())
                .memberLeaveDate(memberInfo.getMemberLeaveDate())
                .memberEnableDate(memberInfo.getMemberEnableDate())
                .build();
    }

    /** 23-8-20
     *  임직원 수정에서 직책 리스트를 위한
     * **/
    public static AdminMemberResp toStaffDto(Staff staff){
        return AdminMemberResp.builder()
                .staffNo(staff.getStaffNo())
                .staffName(staff.getStaffName())
                .build();
    }



    private static String dateToString(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }



}
