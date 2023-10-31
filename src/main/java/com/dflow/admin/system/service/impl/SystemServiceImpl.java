package com.dflow.admin.system.service.impl;

import com.dflow.admin.organization.responseDto.AdminDepartStaffResp;
import com.dflow.admin.organization.responseDto.AdminMemberResp;
import com.dflow.admin.system.responseDto.AuthoSelResp;
import com.dflow.admin.system.service.SystemService;
import com.dflow.entity.MemberInfo;
import com.dflow.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class SystemServiceImpl implements SystemService {


    private final MemberInfoRepository memberInfoRepository;

    /**
     * 23-9-6
     * 접근권한 에서 사용할 멤버 리스트 호출
     * **/
    public List<AuthoSelResp> selAuthoList(){
        List<AuthoSelResp> authoList = memberInfoRepository.findAlLByAndMemberFlagOrderByStaffNoAsc("0")
                .stream().map(AuthoSelResp::toDto).collect(Collectors.toList());
        return authoList;
    }


    /**
     * 23-9-6
     * 접근권한 에서 수정 할 때 값 받아오는
     * **/

    /** 임직원 수정에 사용할 멤버 조회 **/
    public AuthoSelResp getAdminMemberNo(Long memberNo){
        MemberInfo memberInfo = memberInfoRepository.findById(memberNo).orElseThrow(EntityExistsException::new);
        return AuthoSelResp.toDto(memberInfo);
    }

    /** 멤버롤 수정 **/
    public AuthoSelResp udtMemberRole(Long memberNo, String memberRole){
        MemberInfo memberInfo = memberInfoRepository.findById(memberNo).orElseThrow(EntityExistsException::new);
        memberInfo.setMemberRole(memberRole);
        MemberInfo udtMember = memberInfoRepository.save(memberInfo);
        return AuthoSelResp.toDto(udtMember);
    }


}
