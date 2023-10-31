package com.dflow.admin.system.service;

import com.dflow.admin.system.responseDto.AuthoSelResp;

import java.util.List;

public interface SystemService {

    // 접근 권한 직책별 리스트 출력
    List<AuthoSelResp> selAuthoList();

    // 수정할 memberNo
    AuthoSelResp udtMemberRole(Long memberNo, String memberRole);

    AuthoSelResp getAdminMemberNo(Long memberNo);

}
