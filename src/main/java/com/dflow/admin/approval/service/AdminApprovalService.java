package com.dflow.admin.approval.service;


import com.dflow.admin.approval.requestDto.AdminDocTypeInsReq;
import com.dflow.admin.approval.requestDto.AdminDocTypeUdtReq;
import com.dflow.admin.approval.requestDto.DocTypeFolderReq;
import com.dflow.admin.approval.requestDto.UdtFolderReq;
import com.dflow.admin.approval.responseDto.*;
import com.dflow.approval.requestDto.DocAprvSearch;
import com.dflow.approval.responseDto.DocAprvSelResp;
import com.dflow.approval.responseDto.DocMemberResp;
import com.dflow.approval.responseDto.DocTypeResp;
import com.dflow.entity.DocumentApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;


public interface AdminApprovalService {



    List<DocFolderTreeResp> getFolderDeptNo();

    List<DocFolderTreeResp> getAllFolderList();


    // 사용자 정보 가져오기
    DocMemberResp selAdminInfo(String memberId);

    // 전체 결재 문서 조회
    List<DocAprvSelResp> docAprvSelAllList();

    // 전자결재 문서 상세 조회
    DocAprvSelResp selDocDetail(Long docNo);

    ///////////////////

    // 사용여부 코드 리스트 불러오기
    List<AprvCodeResp> selAprvCodeList();

    // 양식 폴더 리스트 불러오기
    List<AprvFolderResp> selAprvFolderList();

    // 양식 등록
    Boolean insDocType(AdminDocTypeInsReq docTypeInsReq);

    //양식 수정
    Boolean udtDocType(AdminDocTypeUdtReq docTypeUdtReq);

    // 문서 양식 정보 조회
    DocFormResp selDocType(Long docFormNo);

    // 문서 양식 삭제 (flag 변경)
    Boolean deleteDocType(Long docFormNo);

    // 문서 양식 폴더 추가
    Boolean insDocTypeFolder(DocTypeFolderReq docTypeFolderReq);
    // 문서 양식 폴더 명 변경
    Boolean udtDocTypeFolder(UdtFolderReq udtFolderReq);
    // 문서 양식 폴더 삭제
    Boolean delDocTypeFolder(Long docTypeFolderNo);

    // 페이징 관련
    Page<DocumentApproval> selDocList(Pageable pageable, DocAprvSearch search, Principal principal);
    Page<DocAprvSelResp> selDocAprvPaging(Pageable pageable, DocAprvSearch search, Principal principal);

    // 폴더 순서 올림
    Boolean upFolderOrder(Long folderNo);

    // 폴더 순서 내림
    Boolean downFolderOrder(Long folderNo);

    // 문서양식 리스트 조회
    List<DocFormListResp> selDocTypeList();

    // 문서양식 정렬 순서 변경
    Boolean udtDocFormOrder(HashMap<String, Object> paramMap);

}
