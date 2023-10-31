package com.dflow.admin.organization.service.impl;

import com.dflow.admin.organization.responseDto.AdminUdtDepartment;
import com.dflow.admin.organization.service.AdminOrganizationService;
import com.dflow.entity.Department;
import com.dflow.organization.service.OrganizationService;
import com.dflow.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional

class AdminOrganizationServiceImplTest {

//    @Autowired
//    private AdminOrganizationServiceImpl organizationService;
//
//
//    @Test
//    void testGetSameName() {
//
//            // 상위 부서 데이터 생성
//            AdminUdtDepartment parentDepartment1 = new AdminUdtDepartment();
//            parentDepartment1.setDepartmentNo(1L);
//            parentDepartment1.setDepartmentName("부서A");
//
//            AdminUdtDepartment parentDepartment2 = new AdminUdtDepartment();
//            parentDepartment2.setDepartmentNo(2L);
//            parentDepartment2.setDepartmentName("부서B");
//
//            // 하위 부서 데이터 생성
//            AdminUdtDepartment childDepartment1 = new AdminUdtDepartment();
//            childDepartment1.setDepartmentNo(3L);
//            childDepartment1.setDepartmentName("하위부서A");
//
//            AdminUdtDepartment childDepartment2 = new AdminUdtDepartment();
//            childDepartment2.setDepartmentNo(4L);
//            childDepartment2.setDepartmentName("부서A 하위");
//
//            // 상위 부서 리스트 생성
//            List<AdminUdtDepartment> parentDepartmentList = new ArrayList<>();
//            parentDepartmentList.add(parentDepartment1);
//            parentDepartmentList.add(parentDepartment2);
//
//            // 하위 부서 리스트 생성
//            List<AdminUdtDepartment> childDepartmentList = new ArrayList<>();
//            childDepartmentList.add(childDepartment1);
//            childDepartmentList.add(childDepartment2);
//
//
//
//            // 테스트 대상 메소드 호출
//            List<AdminUdtDepartment> result = organizationService.getSameName();
//            System.out.println("검증 됐나?" + result);
//
//            // 결과 검증
//            assertNotNull(result);
//            assertFalse(result.isEmpty());
//            // TODO: 실제 결과와 기대 결과를 비교하여 검증을 수행하세요.
//
//
//    }


}