package com.dflow.admin.organization.service;

import com.dflow.admin.organization.requestDto.AdminDepartmentCreateReq;
import com.dflow.admin.organization.responseDto.*;
import com.dflow.entity.Department;

import java.util.List;


public interface AdminOrganizationService {

   List<AdminDepartmentResp>  getAllDepartment();   // 어드민 페이지 관리자 + 부서조회에서 사용

//    AdminDepartStaffResp getAdminDepartNo(Long departmentNo);   // 부서 고유 번호 가져오는

    AdminDepartmentResp getAdminDepartNo(Long departmentNo);

    AdminUdtDepartment udtAdminDepartment(Long departmentNo, AdminUdtDepartment udtDepartment); // 수정용

    AdminUdtDepartment getUdtAdminDepartmentNo(Long departmentNo);  // 수정할 때 No을 얻을

    List<Department> findAll();

    //상위 부서 셀렉할
    List<AdminUdtDepartment> departmentParentNoList();

    // 하위 부서.
    List<AdminUdtDepartment> departmentChildNoList();
    
    // 상위부서랑 같은 하위 부서 이름 찾기
    List<AdminUdtDepartment> getSameName();

    List<AdminUdtDepartment> getChild(Long no);
    

 // 부서 추가
    AdminDepartmentCreateReq createAdminDepartment(AdminDepartmentCreateReq createDepartment);

    // 부서 삭제
    void deleteDepartment(Long departmentNo);


    //  조직원 조회
    List<AdminDepartStaffResp> toAdminMemberList();

    // 어드민 임직원 조회에 사용
    List<AdminMemberResp> getMemberInfoListByDeptNo(Long departmentNo);

    // 임직원 관리 정보 수정에 사용
    AdminMemberResp udtAdminMember(Long memberNo, AdminMemberResp udtAdminMember);

    // 임직원 수정에 사용할 조회
    AdminMemberResp getAdminMemberNo(Long memberNo);

    // 임직원 부서 리스트 
    List<AdminUdtDepartment> adminMemberDepartList();

    // 임직원 직책 조회 리스트
    List<AdminMemberResp> adminMemberStaffList();

    //부서 위치 위로
    String upDepartmentValue(Long departmentNo);

    // 부서 위치 아래로
    String downDepartmentValue(Long departmentNo);

    // 최하위 부서 찾기
    Department maxQueueValue();

    List<Department> getQueueParent();



}
