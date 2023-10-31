package com.dflow.repository;

import com.dflow.entity.Department;
import com.dflow.entity.MemberInfo;
import com.dflow.repository.querydsl.MemberInfoQuerydsl;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long>, MemberInfoQuerydsl {

    Optional<MemberInfo> findByMemberId(String memberId);

    List<MemberInfo> findAllByDepartmentNoAndMemberFlagOrderByStaffNo(Long departmentNo, String memberFlag);

    List<MemberInfo> findAlLByAndMemberFlagOrderByStaffNoAsc(String memberFlag);

    Optional<MemberInfo> findByMemberNameKr(String memberNameKr);

    MemberInfo findByMemberNo(Long createNo);

    // 전체 조회 사용 관리자에서 사용
    List<MemberInfo> findAllByOrderByStaffNoAsc();

    // 관리자 부서별 조회에서 사용
    List<MemberInfo> findAllByDepartmentNoOrderByStaffNo(Long departmentNo);

    // 왔다 길다
    List<MemberInfo> findAllByDepartment_DepartmentParentNoIsNullOrderByDepartmentNoAscStaff_StaffNoAsc();

    List<MemberInfo> findAllByDepartment_DepartmentParentNoOrderByStaff_StaffNo(Long departmentNo);

    // 퇴사자 아닌 모든 임직원 불러오기
    List<MemberInfo> findAllByMemberFlag(String memberFlag);
}