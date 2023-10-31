package com.dflow.organization.service;

import com.dflow.admin.organization.responseDto.AdminDepartStaffResp;
import com.dflow.entity.Department;
import com.dflow.entity.MemberInfo;
import com.dflow.entity.Staff;
import com.dflow.organization.responseDto.*;
import com.dflow.organization.requestDto.OrgSearchReq;

import com.dflow.organization.responseDto.DepartStaffMemberResp;
import com.dflow.organization.responseDto.DeptTreeResp;
import com.dflow.organization.responseDto.OrgDetail;

import java.util.List;

public interface OrganizationService {

    /** 부서 트리 **/
    List<DeptTreeResp> getDeptTreeResp();

    /** 전체 직원 조회 **/
    List<DepartStaffMemberResp> getAllMemberList();

    /** 직원 상세 정보 조회 **/
    OrgDetail selOrgDetail(Long memberNo);

    /** 하위부서 번호로 직원리시트 조회 **/
    List<DepartStaffMemberResp> getMemberInfoListByDeptNo(Long departmentNo);

    /** 검색 조회 **/
    List<DepartStaffMemberResp> searchMemberList(OrgSearchReq orgSearchReq);

    /** 상위부서 번호로 직원리시트 조회 **/
    List<DepartStaffMemberResp> getMemberInfoListByUpDeptNo(Long departmentNo);

}
