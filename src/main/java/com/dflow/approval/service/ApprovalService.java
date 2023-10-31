package com.dflow.approval.service;

import com.dflow.approval.requestDto.*;
import com.dflow.approval.responseDto.*;
import com.dflow.board.responseDto.AttachFileInfo;
import com.dflow.approval.responseDto.DocAprvSelResp;
import com.dflow.approval.responseDto.DocMemberResp;
import com.dflow.approval.responseDto.DocTypeDetailResp;
import com.dflow.approval.responseDto.DocTypeResp;
import com.dflow.entity.DocumentApproval;
import com.dflow.project.responseDto.RespProjCode;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface ApprovalService {
    List<DocAprvSelResp> docAprvSelAllList();

    List<DocTypeResp> selDocTypeList();
    List<RespProjCode> selAnnualCodeType();

    DocTypeDetailResp selDocTypeDetail(Long docFormTypeNo);
    Boolean insDocAprv(DocAprvInsReq docReq, MultipartFile[] uploadFile, String memberName);

    //전자결재문서 임시저장 등록
    Boolean insTempDocAprv(TempDocAprvReq tempDocAprvReq);

    //전자결재문서 임시저장 수정
    Boolean udtTempDocAprv(TempDocAprvReq tempDocAprvReq);

    //전자결재문서 수정
    Boolean udtDocAprv(DocAprvInsReq req, Long docNo);
    //전자결재문서 첨부파일 등록
    Boolean fileUpload(MultipartFile[] uploadFile, Long docNo, String memberId);
    //기존 첨부파일 삭제
    Boolean delFiles(List<Long> delFiles);
    DocMemberResp selMemberInfo(String memberId);

    // 전자결재문서 상세 조회
    DocAprvSelResp selDocDetail(Long docNo);

    // 전자결재문서 삭제
    DocAprvSelResp delDocAprv(Long docNo);

    String getSpanTxt(Document doc, String id);

    List<DocAprvCodeResp> selDocAprvCodeList(String codeType);

    Boolean udtAprv(AprvUdtReq aprvUdtReq);
    Boolean insLeaveReacord(DocumentApproval document);

    Page<DocumentApproval> selDocList(Pageable pageable, DocAprvSearch search, Principal principal);

    Page<DocAprvSelResp> selDocAprvPaging(Pageable pageable, DocAprvSearch search, Principal principal);

    TempDocAprvResp<TempDocAprvInfoResp> tempPageList(TempDocAprvPageReq tempDocAprvPageReq, String memberId);

    // 최종 검증
    // List<DocAprvCodeResp> codeCheck(String codeType);

//    // docAprv state 별
//    String selDocState(DocAprvSearch search, String docState);

    // docAprv state 별
    String selDocState(DocAprvSearch search, String docState);

    // docAprv state 별로 문서 세팅
//    String setDocType(DocAprvSearch search, String docState);
    String setDocType(DocAprvSearch search, String docState);


    // 임시 저장 문서 불러오기
    TempDocAprvDetailResp selTempDocAprv(Long docNo);



    boolean saveSignToMember(String memberId, MultipartFile signImg);

    boolean deleteSignToMember(String memberId);

    Boolean udtWithdrawalAprv(Long docNo);

    List<Long> selDocFileNoList(Long docNo);

    List<AttachFileInfo> selAttachFileList(Long docNo);
}
