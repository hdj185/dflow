package com.dflow.login.service.impl;

import com.dflow.attachFile.service.AttachFileService;
import com.dflow.config.SecurityConfig;
import com.dflow.entity.AnnualLeave;
import com.dflow.entity.CodeInfo;
import com.dflow.entity.Department;
import com.dflow.entity.MemberInfo;
import com.dflow.login.requestDto.AuthMemberRequest;
import com.dflow.login.responseDto.RespDeptLogin;
import com.dflow.login.responseDto.RespMdfMember;
import com.dflow.login.responseDto.RespStfLogin;
import com.dflow.login.service.LoginService;
import com.dflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final MemberInfoRepository memberInfoRepository;

    private final DepartmentRepository departmentRepository;

    private final StaffRepository staffRepository;

    private final PasswordEncoder passwordEncoder;
    private final AttachFileService attachFileService;
    private final AnnualLeaveRepository annualLeaveRepository;
    private final CodeInfoRepository codeInfoRepository;


//    public MemberInfo saveMember(MemberInfo member) {
//        validateDuplicateMember(member);
//        return memberInfoRepository.save(member);
//    }

    public boolean validateDuplicateMember(AuthMemberRequest authMemberRequest) {
        Optional<MemberInfo> optionalMember = memberInfoRepository.findByMemberId(authMemberRequest.getMemberId());

        return optionalMember.isPresent();

//        if (findMember != null) {
//            throw new IllegalStateException("이미 등록된 직원입니다.");
//        }
    }

    public boolean validateRetiredMember(String memberId) {

        MemberInfo memberInfo = memberInfoRepository.findByMemberId(memberId).get();

        if (memberInfo.getMemberFlag().equals("퇴사직원")) {

            return true;
        }

        return false;
    }

    public boolean registerMember(AuthMemberRequest authMemberRequest, MultipartFile memberImg) {

        try {
            // Member 엔티티를 생성하고 받은 데이터를 넣어줍니다.
            authMemberRequest.setMemberFlag("0");
            MemberInfo member = authMemberRequest.toEntity(passwordEncoder);

            // 파일 저장
            if(memberImg != null && !memberImg.isEmpty()) {
                Long imgNo = attachFileService.insFile(memberImg).getFileAttachNo();
                member.changeImgAttachNo(imgNo);
            }
//            else {
//                Optional<CodeInfo> optional = codeInfoRepository.findCodeInfoByCodeTypeAndCodeName("MEMBER_IMG", "DEFAULT_IMG");
//                if(optional.isPresent()) {
//                    CodeInfo code = optional.get();
//                    member.changeImgAttachNo(Long.parseLong(code.getCodeAccount()));
//                }
//            }

            // Member 엔티티를 DB에 저장합니다.
            MemberInfo memberInfo = memberInfoRepository.save(member);

            // 기본 연차 정보 생성
            AnnualLeave annualLeave = AnnualLeave.builder()
                    .annualCount(0D)
                    .annualLeft(0D)
                    .annualType("")
                    .annualEndDate(LocalDate.of(LocalDate.now().getYear(), 12, 31))
                    .memberNo(memberInfo.getMemberNo())
                    .build();
            annualLeaveRepository.save(annualLeave);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 회원 정보 수정용 MemberInfo 받아오기
    public RespMdfMember selModifyMemberInfo(String memberId) {
        MemberInfo member = memberInfoRepository.findByMemberId(memberId).get();
        RespMdfMember resp = RespMdfMember.of(member);

        return resp;
    }

    // 회원 정보 수정 후 저장
    public boolean saveModifiedMember(AuthMemberRequest authMemberRequest, MultipartFile memberImg) {

        MemberInfo member = memberInfoRepository.findByMemberId(authMemberRequest.getMemberId()).get();

//        member.changeMemberPw(member.getMemberPw());

        //파일 저장
        if(memberImg != null && !memberImg.isEmpty()) {
            Long imgNo = attachFileService.insFile(memberImg).getFileAttachNo();
            member.changeImgAttachNo(imgNo);    //저장된 이미지 번호 넣기
        }

        member.changeMemberPhone(authMemberRequest.getMemberPhone());
        member.changeMemberEmail(authMemberRequest.getMemberEmail());
        member.changeMemberAddress(authMemberRequest.getMemberAddress());
        member.changeMemberLeaveDate(authMemberRequest.getMemberLeaveDate());

        try {
            memberInfoRepository.save(member);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 비밀번호 일치 확인
    @Override
    public boolean checkPassword(String memberId, String checkPassword) {
        MemberInfo member = memberInfoRepository.findByMemberId(memberId)
                .orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        String realPassword = member.getMemberPw();
        boolean matches = passwordEncoder.matches(checkPassword, realPassword);
        return matches;
    }

    // 비밀번호 변경
    @Override
    public boolean changePassword(String memberId, String memberPw) {

        try {
            /* 회원 찾기 */
            Optional<MemberInfo> optional = memberInfoRepository.findByMemberId(memberId);

            System.out.println("여기 체인지 비번 " +  memberId);
            MemberInfo member = optional.orElseThrow(() ->
                    new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
            );

            /* 수정한 비밀번호 암호화 */
            String encryptPassword = passwordEncoder.encode(memberPw);
            member.changeMemberPw(encryptPassword); // 회원 수정

            return true;
        } catch (IllegalArgumentException e) {
            // 해당 회원이 존재하지 않을 때 예외 처리
            e.printStackTrace(); // 또는 로그에 기록하거나 적절한 처리를 수행
            return false; // 예외 상황을 나타내는 값을 반환하거나 다른 처리를 수행
        }
    }

    // 회원가입 부서 셀렉트박스용
    public List<RespDeptLogin> selDepartmentList() {

        //우선순위 반영 수정
        //부서 리스트 가져온다. false == 사용 , true == 미사용
        List<Department> departmentList = departmentRepository.findAll();

        departmentList.sort(
            Comparator.comparing(Department::getDepartmentDepth)
                            .thenComparing(Department::getQueueValue, Comparator.nullsLast(Comparator.naturalOrder()))

        );

        return RespDeptLogin.of(departmentList);
    }

    // 회원가입 직책 셀렉트박스용
    public List<RespStfLogin> selStaffList() {


        return RespStfLogin.of(staffRepository.findAllByOrderByStaffNoAsc());
    }

    // 임시 비번 발급용 가입 정보 확인
    public boolean memberEmailCheck(String memberId, String memberEmail, String memberNameKr) {

        MemberInfo memberInfo = memberInfoRepository.findByMemberId(memberId).orElseThrow(() ->
                new IllegalArgumentException("일치하는 정보가 없습니다."));

        if (memberInfo != null && memberInfo.getMemberNameKr().equals(memberNameKr) && memberInfo.getMemberEmail().equals(memberEmail)) {
            return true;
        } else {
            return false;
        }
    }


}
