package com.dflow.admin.organization.responseDto;


import com.dflow.entity.Department;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * 여기는 부모 no 빠져있음
 *
 */

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminDepartmentResp {

    private Long departmentNo;

    private String departmentName;                                                      //부서명

    private String departmentParentName;                                                // 상위부서명

    private Integer departmentDepth;

    private boolean departmentFlag;                                                     //사용여부

    private Long updateNo;                                                              //수정자 번호

    private String updateDate;


    /** 어드민에 departmentNo을 가져오기 위한   **/
    public static AdminDepartmentResp adminDepartment(Department department){
        return AdminDepartmentResp.builder()
            .departmentNo(department.getDepartmentNo())
            .departmentName(department.getDepartmentName())
            .departmentParentName(department.getDepartmentParentNo() != null ? department.getDepartmentParentNo().getDepartmentName() : null )
            .departmentDepth(department.getDepartmentDepth())
            .updateNo(department.getUpdateNo())
            .updateDate(dateToString(department.getUpdateDate()))
            .departmentFlag(department.isDepartmentFlag())

            .build();
    }

    private static String dateToString(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
