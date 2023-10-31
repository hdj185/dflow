package com.dflow.repository.querydsl;

import com.dflow.entity.MemberInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberInfoQuerydsl {


    List<MemberInfo> findAllByDepartmentNo(Long departmentNo);
    List<MemberInfo> searchDeptToMember(String[] types, String keyword);
}
