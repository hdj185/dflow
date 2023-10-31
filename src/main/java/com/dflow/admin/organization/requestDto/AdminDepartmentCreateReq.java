package com.dflow.admin.organization.requestDto;


import com.dflow.admin.organization.responseDto.AdminUdtDepartment;
import com.dflow.entity.Board;
import com.dflow.entity.Department;
import com.dflow.entity.MemberInfo;
import lombok.*;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.time.LocalDateTime;


/***
 * 23-8-13
 * 부서 추가
 */


@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdminDepartmentCreateReq {


    /** 부서 고유 번호 **/
    private Long departmentNo;

    /** 상위부서 고유 번호 **/
    private Long departmentParentNo;

    /** 부서 명 **/
    @NotBlank(message = "부서명 필 수 입력")
    private String departmentName;

    /** 부서 계층 **/
    private Integer departmentDepth;

    /** 부서 활성 여부 **/
    private boolean departmentFlag;

    /** 우선순위 설정 **/
    private Long queueValue;

    private Long childQueueValue;


    public static AdminDepartmentCreateReq toAdminDepartmentCreateReq(Department department){

        return AdminDepartmentCreateReq.builder()

            .departmentNo(department.getDepartmentNo())
            .departmentName(department.getDepartmentName())
            .departmentParentNo(department.getDepartmentParentNo() != null ? department.getDepartmentParentNo().getDepartmentNo() : null)
            .departmentDepth(department.getDepartmentDepth())
            .departmentFlag(department.isDepartmentFlag())
            .queueValue(department.getQueueValue())
            .build();
    }


    public static AdminDepartmentCreateReq toQueue(Department department){
        return AdminDepartmentCreateReq.builder()
            .departmentNo(department.getDepartmentNo())
            .departmentName(department.getDepartmentName())
            .departmentParentNo(department.getDepartmentParentNo() != null ? department.getDepartmentParentNo().getDepartmentNo() : null)
            .departmentDepth(department.getDepartmentDepth())
            .departmentFlag(department.isDepartmentFlag())
            .queueValue(department.getQueueValue())
            .queueValue(department.getQueueValue())
            .childQueueValue(department.getChildQueueValue())
            .build();
    }

}
