package com.dflow.repository;

import com.dflow.admin.organization.requestDto.AdminDepartmentCreateReq;
import com.dflow.entity.Department;
import com.dflow.entity.MemberInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @EntityGraph(attributePaths = "children", type = EntityGraph.EntityGraphType.LOAD)
    List<Department> findByDepartmentParentNoIsNullAndDepartmentFlag(boolean departmentFlag);


    List<Department> findAllByDepartmentNoOrderByDepartmentNo(Long departmentNo);


    // 상위부서 departmentNo 가져오기  // 부모가 null 일 경우  찾아오는 메서드
    List<Department> findDepartmentByDepartmentParentNoIsNull();

    // 하위부서
    List<Department> findDepartmentByDepartmentParentNoIsNotNull();

    List<Department> findByDepartmentParentNo(Long no);

    ///사용해야 하는데 아직 미사용
    @Query("select MemberInfo.memberNameKr from Department d join d.createInfo MemberInfo where d.createNo = :createNo")
    String findMemberNameByCreateNo(Long createNo);

    List<Department>  findByDepartmentNoNotNullAndDepartmentFlag(boolean departmentFlag);

    List<Department> findByQueueValueNotNull();

    // 우선순위용
    Department findByQueueValue(Long queueValue);

    // 최하위 부서 찾기
    Optional<Department> findFirstByOrderByQueueValueDesc();


    // 하위부서가 있는 상위 부서 위로 옮기기 위한  queue
    List<Department> findAllByQueueValue(Long queueValue);

}
