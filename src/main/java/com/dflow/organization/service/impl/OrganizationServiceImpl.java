package com.dflow.organization.service.impl;

import com.dflow.admin.organization.responseDto.AdminDepartStaffResp;
import com.dflow.admin.organization.responseDto.AdminDepartmentResp;
import com.dflow.entity.Department;
import com.dflow.entity.MemberInfo;
import com.dflow.organization.responseDto.*;
import com.dflow.entity.Staff;
import com.dflow.organization.requestDto.OrgSearchReq;

import com.dflow.organization.responseDto.DepartStaffMemberResp;
import com.dflow.organization.responseDto.DeptTreeResp;
import com.dflow.organization.responseDto.OrgDetail;
import com.dflow.organization.service.OrganizationService;
import com.dflow.repository.DepartmentRepository;
import com.dflow.repository.MemberInfoRepository;
import com.dflow.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final DepartmentRepository departmentRepository;

    private final MemberInfoRepository memberInfoRepository;

    private final StaffRepository staffRepository;

    @Override
    public List<DeptTreeResp> getDeptTreeResp() {

        List<DeptTreeResp> deptTreeRespList = departmentRepository
                .findByDepartmentParentNoIsNullAndDepartmentFlag(false)
                .stream().map(DeptTreeResp::toDto)
                .collect(Collectors.toList());



        for(DeptTreeResp de : deptTreeRespList) {
            System.out.println(deptTreeRespList);
        }

        System.out.println(deptTreeRespList + "dddddddddddddddddddddddddddddd");

        // 하위 부서 정렬 
        for (DeptTreeResp deptTreeResp : deptTreeRespList) {
            Collections.sort(deptTreeResp.getChildren(), Comparator.comparing(DeptTreeResp::getDepartmentName));
        }

        System.out.println(deptTreeRespList + " -------------------------------------------------------getTree");

        // 상위부서 정렬
        Collections.sort(deptTreeRespList, Comparator.comparing(DeptTreeResp::getDepartmentName));

        // 상위 부서 우선순위 정렬 추가 ---
        deptTreeRespList.sort(
                Comparator.comparing(DeptTreeResp::getQueueValue, Comparator.nullsLast(Comparator.naturalOrder()))
        );

        return deptTreeRespList;
    }

    /** 전체 맴버 entity 리스트를 dto 리스트로 변환 **/
    @Override
    public List<DepartStaffMemberResp> getAllMemberList() {

        List<DepartStaffMemberResp> allMemberList = memberInfoRepository
                .findAlLByAndMemberFlagOrderByStaffNoAsc("0")
                .stream().map(DepartStaffMemberResp::toDto)
                .collect(Collectors.toList());
        return allMemberList;

    }

    // 뭐든 찾아온다 디티오를
    @Override
    public OrgDetail selOrgDetail(Long memberNo) {
        MemberInfo memberInfo = memberInfoRepository.findById(memberNo)
                .orElseThrow(EntityExistsException::new);
        return OrgDetail.toDto(memberInfo);
    }

    /** 부서별 직원 리스트 직책별 오름차순 조회 **/
    @Override
    public List<DepartStaffMemberResp> getMemberInfoListByDeptNo(Long departmentNo) {

        List<DepartStaffMemberResp> memberInfoList = memberInfoRepository
                .findAllByDepartmentNoAndMemberFlagOrderByStaffNo(departmentNo, "0")
                .stream()
                .map(DepartStaffMemberResp::toDto)
                .collect(Collectors.toList());

        return memberInfoList;
    }

    @Override
    public List<DepartStaffMemberResp> searchMemberList(OrgSearchReq orgSearchReq) {

        String[] types = orgSearchReq.getTypes();
        String keyword = orgSearchReq.getKeyword();

        List<DepartStaffMemberResp> memberList = memberInfoRepository.searchDeptToMember(types, keyword)
                .stream()
                .map(DepartStaffMemberResp::toDto)
                .collect(Collectors.toList());

        return memberList;
    }

    @Override
    public List<DepartStaffMemberResp> getMemberInfoListByUpDeptNo(Long departmentNo) {

        List<DepartStaffMemberResp> memberInfoList = memberInfoRepository
                .findAllByDepartmentNoAndMemberFlagOrderByStaffNo(departmentNo, "0")
                .stream()
                .map(DepartStaffMemberResp::toDto)
                .collect(Collectors.toList());

        Department department = departmentRepository.findById(departmentNo).orElseThrow();
        for (int i = 0; i < department.getChildren().size(); i++) {
            List<DepartStaffMemberResp> childMemberInfoList = memberInfoRepository
                    .findAllByDepartmentNoAndMemberFlagOrderByStaffNo(department.getChildren().get(i).getDepartmentNo(), "0")
                    .stream()
                    .map(DepartStaffMemberResp::toDto)
                    .collect(Collectors.toList());

            memberInfoList.addAll(childMemberInfoList);
        }

        return memberInfoList;
    }
}
