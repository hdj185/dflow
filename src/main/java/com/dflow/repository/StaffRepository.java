package com.dflow.repository;

import com.dflow.entity.Staff;
import com.dflow.organization.responseDto.DepartStaffMemberResp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findAllByOrderByStaffNoAsc();


}
