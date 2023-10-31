package com.dflow.project.responseDto;

import com.dflow.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RespProjDept {
    /** 부서 고유 번호 **/
    private Long departmentNo;

    /** 부서 명 **/
    private String departmentName;

    /** 부서 계층 **/
    private Integer depth;

    /** 부서 우선순위 결정 **/
    private Long queueValue;

    /** 하위 부서 리스트 **/
    private List<RespProjDept> children;
    private List<RespProjMember> members;

    // 부서 번호, 이름, 계층번호, 자식 개체를 가져와서 dto로 변환
    public static RespProjDept of(Department department){

        List<RespProjMember> sortedMembers = department.getMembers().stream()
                .map(RespProjMember::of)
                .sorted(Comparator.comparing(RespProjMember::getStaffNo, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        return new RespProjDept(
                department.getDepartmentNo(),
                department.getDepartmentName(),
                department.getDepartmentDepth(),
                department.getQueueValue(),
                department.getChildren().stream().map(RespProjDept::of).collect(Collectors.toList()),
                sortedMembers
        );
    }

    //entity -> dto
    public static List<RespProjDept> of(List<Department> entityList){
        List<RespProjDept> list = new ArrayList<>();
        for(Department department : entityList)
            list.add(of(department));
        return list;
    }
}
