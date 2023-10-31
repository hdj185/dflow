package com.dflow.organization.responseDto;

import com.dflow.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeptTreeResp {

    /** 부서 고유 번호 **/
    private Long departmentNo;

    /** 부서 명 **/
    private String departmentName;

    /** 부서 계층 **/
    private Integer depth;

   /** 부서 활성 유무 **/
    private boolean departmentFlag;

    private Long queueValue;

    /** 하위 부서 리스트 **/
    private List<DeptTreeResp> children;



    // 부서 번호, 이름, 계층번호, 자식 개체를 가져와서 dto로 변환
    public static DeptTreeResp toDto(Department department){
        return new DeptTreeResp(
                department.getDepartmentNo(),
                department.getDepartmentName(),
                department.getDepartmentDepth(),
                department.isDepartmentFlag(),
                department.getQueueValue(),
                department.getChildren().stream().map(DeptTreeResp::toDto).collect(Collectors.toList())
        );
    }

}
