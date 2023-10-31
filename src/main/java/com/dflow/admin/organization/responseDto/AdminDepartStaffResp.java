package com.dflow.admin.organization.responseDto;

import com.dflow.entity.Department;
import com.dflow.entity.MemberInfo;
import com.dflow.login.CustomUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 관리자 부서조회에서 사용
 * 생성자 생성일 추가 사용
 * **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDepartStaffResp {


    /** 멤버 번호 **/
    private Long memberNo;

    /** 직책 번호 **/
    private Long staffNo;

    /** 부서 번호**/
    private Long departmentNo;

    private Long departmentParentNo;

    /** 부서명 **/
    private String departmentName;

    /** 수정자 **/
    private String updateName;

    /**  수정일 **/
    private String updateDate;

    /** 이름 **/
    private String memberNameKr;

    /** 직책 **/
    private String staffName;

    /** 활성여부 **/
    private boolean departmentFlag;   ;   // 활성화 여부

    /** 멤버 활성 여부 **/
    private String memberFlag;

    /**멤버 입사일.. null = false 라 입력해줘야 함..**/
    private LocalDate memberEnableDate;


    /** 멤버 id 찾을 용도로 dto 변환   **/
    public static AdminDepartStaffResp toAdminDepartNo(Department department){
        return AdminDepartStaffResp.builder()
            .departmentNo(department.getDepartmentNo())
            .departmentName(department.getDepartmentName())
            .updateName(department.getCreateInfo().getMemberNameKr())
            .updateDate(dateToString(department.getUpdateDate()))
            .departmentFlag(department.isDepartmentFlag())
            .build();
    }


    /** 어드민에 조직원 관리에 넣을 값을 dto로 변환 **/
    public static AdminDepartStaffResp toAdminMemberList(MemberInfo memberInfo){

        return AdminDepartStaffResp.builder()
            .memberNo(memberInfo.getMemberNo())
            .staffNo(memberInfo.getStaff().getStaffNo())
            .departmentName(memberInfo.getDepartment().getDepartmentName())
            .updateDate(dateToString(memberInfo.getUpdateDate()))
            .memberNameKr(memberInfo.getMemberNameKr())
            .staffName(memberInfo.getStaff().getStaffName())
            .memberFlag(memberInfo.getMemberFlag())
            .memberEnableDate(memberInfo.getMemberEnableDate())
            .build();
    }







    private static String dateToString(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
