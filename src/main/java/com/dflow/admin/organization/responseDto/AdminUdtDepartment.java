package com.dflow.admin.organization.responseDto;

import com.dflow.entity.Department;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AdminUdtDepartment {

    private String departmentName;
    private Long departmentNo;
    private Long departmentParentNo;
    private Integer departmentDepth;
    private boolean departmentFlag;



    /**
     * 23-8-9
     * 부서 수정을 위한 메서드
     * **/
    public static AdminUdtDepartment toAdminUdtDepartmentDto(Department department){
        return AdminUdtDepartment.builder()
                .departmentNo(department.getDepartmentNo())
                .departmentName(department.getDepartmentName())
                .departmentParentNo(department.getDepartmentParentNo() != null ? department.getDepartmentParentNo().getDepartmentNo() : null)
                .departmentDepth(department.getDepartmentDepth())
                .departmentFlag(department.isDepartmentFlag())
                .build();
    }

    /** 23-8-9
     *  부서  넘버를 활용하기 위한
     * **/
    public static AdminUdtDepartment getAdminDepartmentNo(Department department){
        AdminUdtDepartment adminUdtDepartment = new AdminUdtDepartment();
        adminUdtDepartment.setDepartmentParentNo(adminUdtDepartment.getDepartmentParentNo());
        adminUdtDepartment.setDepartmentNo(department.getDepartmentNo());
        adminUdtDepartment.setDepartmentName(department.getDepartmentName());
        adminUdtDepartment.setDepartmentDepth(department.getDepartmentDepth());
        adminUdtDepartment.setDepartmentFlag(department.isDepartmentFlag());

        // 상위부서가 있을 경우
        if(department.getDepartmentParentNo() != null){
            adminUdtDepartment.setDepartmentParentNo(department.getDepartmentParentNo().getDepartmentNo());
        }
        return adminUdtDepartment;
    }
}
