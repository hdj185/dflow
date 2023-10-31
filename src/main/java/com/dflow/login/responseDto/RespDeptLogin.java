package com.dflow.login.responseDto;

import com.dflow.entity.Department;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespDeptLogin {

    private Long departmentNo;

    private String departmentName;

    // 우선순위 용 추가
    private Long queueValue;

    // entity -> dto
    public static RespDeptLogin of(Department department) {

        return RespDeptLogin.builder()
            .departmentNo(department.getDepartmentNo())
            .departmentName(department.getDepartmentName())
            .queueValue(department.getQueueValue())
            .build();
    }

    // entity list -> dto list
    public static List<RespDeptLogin> of(List<Department> departments) {
        List<RespDeptLogin> list = new ArrayList<>();
        for (Department entity : departments)
            list.add(of(entity));

        return list;
    }

}
