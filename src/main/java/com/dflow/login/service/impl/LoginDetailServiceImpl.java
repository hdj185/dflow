package com.dflow.login.service.impl;

import com.dflow.entity.MemberInfo;
import com.dflow.login.CustomUser;
import com.dflow.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoginDetailServiceImpl implements UserDetailsService {

    private final MemberInfoRepository memberInfoRepository;

    // UserDetails 생성 (로그인한 사람 찾기)
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException, DisabledException {

        MemberInfo member = memberInfoRepository.findByMemberId(memberId).get();

        if (member == null) {
            throw new UsernameNotFoundException(memberId);
        }

        if (member.getMemberFlag().equals("퇴사직원")) {
            throw new UsernameNotFoundException(memberId);
        }

//        return User.builder()
//                .username(member.getMemberId())
//                .password(member.getMemberPw())
//                .roles(member.getMemberRole())
//                .build();
        return new CustomUser(member.getMemberId(),
                member.getMemberPw(),
                member.getMemberRole(),
                member.getMemberNo(),
                member.getMemberNameKr());
    }

}
