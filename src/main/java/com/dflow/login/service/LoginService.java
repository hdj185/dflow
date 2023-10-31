package com.dflow.login.service;


import com.dflow.login.requestDto.AuthMemberRequest;
import com.dflow.login.responseDto.RespDeptLogin;
import com.dflow.login.responseDto.RespMdfMember;
import com.dflow.login.responseDto.RespStfLogin;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LoginService {

    public boolean validateDuplicateMember(AuthMemberRequest authMemberRequest);

    // 퇴사 직원 확인
    public boolean validateRetiredMember(String memberId);

    // 멤버 등록
    public boolean registerMember(AuthMemberRequest authMemberRequest, MultipartFile memberImg);

    // 비밀번호 변경
    boolean changePassword(String memberId, String memberPw);

    // 회원가입 부서 셀렉트박스용
    public List<RespDeptLogin> selDepartmentList();

    // 회원가입 직책 셀렉트박스용
    public List<RespStfLogin> selStaffList();

    // 회원 정보 수정용
    RespMdfMember selModifyMemberInfo(String memberId);

    public boolean saveModifiedMember(AuthMemberRequest authMemberRequest, MultipartFile memberImg);

    // 비밀번호 확인
    public boolean checkPassword(String memberId, String checkPassword);

    // 임시 비번 발급용 가입 정보 확인
    boolean memberEmailCheck(String memberId, String memberEmail, String memberNameKr);
}
