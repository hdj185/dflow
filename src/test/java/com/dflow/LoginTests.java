package com.dflow;

import com.dflow.entity.MemberInfo;
import com.dflow.login.requestDto.AuthMemberRequest;
import com.dflow.login.service.impl.LoginServiceImpl;
import com.dflow.repository.DepartmentRepository;
import com.dflow.repository.MemberInfoRepository;
import com.dflow.repository.StaffRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
public class LoginTests {

    @Autowired
    MemberInfoRepository memberInfoRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    LoginServiceImpl loginServiceImpl;

    @Autowired
    PasswordEncoder passwordEncoder;

//    @Test
//    void dtoTest() {
//        AuthMemberRequest memberDto = new AuthMemberRequest();
//
//        memberDto.setMemberId("gildong");
//        memberDto.setMemberPw("gildong2");
//        memberDto.setMemberNameKr("홍길동");
//        memberDto.setMemberGender("남");
//        memberDto.setMemberPhone("010-1717-1771");
//        memberDto.setMemberBirthdate(LocalDate.now());
//        memberDto.setMemberEnableDate(LocalDate.now());
//        memberDto.setDepartmentNo(1L);
//        memberDto.setStaffNo(1L);
//
//        MemberInfo member = memberDto.toEntity(passwordEncoder);
////        member.changeDepartmentNo(memberDto.getDepartmentNo());
////        member.changeStaffNo(memberDto.getStaffNo());
//
//
//        System.out.println("-----------------------------여기-------------------------------");
//        System.out.println(member.toString());
////        System.out.println(member.getDepartmentNo());
//        System.out.println(member.getMemberPw());
//        System.out.println("-----------------------------여기-------------------------------");
//
//       memberInfoRepository.save(member);
//    }

}
