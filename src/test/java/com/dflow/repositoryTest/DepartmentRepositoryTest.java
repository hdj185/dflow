package com.dflow.repositoryTest;

import com.dflow.entity.Department;
import com.dflow.organization.responseDto.DeptTreeResp;
import com.dflow.repository.DepartmentRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Log4j2
public class DepartmentRepositoryTest {

//    @Autowired
//    private DepartmentRepository departmentRepository;
//
//    @Test
//    @Transactional(readOnly = true)
//    public void test(){
//        List<DeptTreeResp> result = departmentRepository.findByDepartmentParentNoIsNullAndDepartmentFlag(false)
//                            .stream().map(DeptTreeResp::toDto)
//                            .collect(Collectors.toList());
//        log.info(result);
//    }
//
//    @Test
//    public void 소속직원출력테스트() {
//        Department department = departmentRepository.findById(1L).get();
//        System.out.println("test--------------------------------");
//        System.out.println("부서명: " + department.getDepartmentName());
//
//    }
//
//    @Test
//    public void 부서전체조회() {
//       List<Department> result = departmentRepository.findAll();
//       for(int i = 0; i < result.size(); i++){
//           log.info(result.get(i).getDepartmentName());
//       }
//
//    }
//
//    @Test
//    public void test1() {
//        Long no = 5L;
//        Department department = departmentRepository.findById(no).get();
//        log.info(department.getDepartmentName());
//
//    }
}
