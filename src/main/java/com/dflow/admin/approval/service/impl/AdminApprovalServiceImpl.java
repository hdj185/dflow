package com.dflow.admin.approval.service.impl;


import com.dflow.admin.approval.requestDto.AdminDocTypeInsReq;
import com.dflow.admin.approval.requestDto.AdminDocTypeUdtReq;
import com.dflow.admin.approval.requestDto.DocTypeFolderReq;
import com.dflow.admin.approval.requestDto.UdtFolderReq;
import com.dflow.admin.approval.responseDto.*;
import com.dflow.admin.approval.service.AdminApprovalService;
import com.dflow.approval.requestDto.DocAprvSearch;
import com.dflow.approval.responseDto.DocAprvSelResp;
import com.dflow.approval.responseDto.DocMemberResp;
import com.dflow.approval.responseDto.DocTypeResp;
import com.dflow.attachFile.service.AttachFileService;
import com.dflow.entity.*;
import com.dflow.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class AdminApprovalServiceImpl implements AdminApprovalService {

    private final CodeInfoRepository codeInfoRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final DocumentTypeFolderRepository documentTypeFolderRepository;
    private final DocumentApprovalRepository documentApprovalRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final AttachFileRepository attachFileRepository;
    private final AttachFileService attachFileService;

    // 문서 전체 조회
    public List<DocAprvSelResp> docAprvSelAllList() {

        List<DocAprvSelResp> adminDocList = documentApprovalRepository
                .findAllByDocFlag("Y")
                .stream()
                .map(DocAprvSelResp::toDto)
                .collect(Collectors.toList());

        return adminDocList;
    }

    // 회원 정보 가져오기
    public DocMemberResp selAdminInfo(String memberId) {
        return DocMemberResp.of(memberInfoRepository.findByMemberId(memberId).get());
    }

    // 전자결재 문서 상세조회
    public DocAprvSelResp selDocDetail(Long docNo) {
        Optional<DocumentApproval> docAprv = documentApprovalRepository.findById(docNo);

        if (!docAprv.isPresent()) return null;
        DocumentApproval entity = docAprv.get();

        return DocAprvSelResp.toDto(entity);
    }

    /** 폴더 문서 트리 조회  **/
    public List<DocFolderTreeResp> getFolderDeptNo(){

        // 상위 문서 설정 - 상위 부서가 null 인 친구들은 상위 부서로 설정
        List<DocFolderTreeResp> folderTreeRespList = documentTypeFolderRepository
                .findByParentFolderIsNullAndTypeFolderFlag("Y")
                .stream().map(DocFolderTreeResp::toDto)
                .collect(Collectors.toList());

        return  folderTreeRespList;

    }

    public List<DocFolderTreeResp> getAllFolderList(){

        List<DocFolderTreeResp> allFolderList = documentTypeFolderRepository.findAllByTypeFolderFlag("Y")
                .stream()
                .map(DocFolderTreeResp::toDto)
                .collect(Collectors.toList());

        return allFolderList;


    }

    ///////////////

    // 사용여부 코드 리스트 구하기
    @Override
    public List<AprvCodeResp> selAprvCodeList() {
        List<CodeInfo> codeInfoList = codeInfoRepository.findCodeInfoByCodeTypeAndCodeFlagFalse("COMMON_CODE");
        return AprvCodeResp.of(codeInfoList);
    }

    // 양식폴더 리스트 구하기
    @Override
    public List<AprvFolderResp> selAprvFolderList() {
        List<DocumentTypeFolder> folderList =  documentTypeFolderRepository.findByDepthAndTypeFolderFlagOrderByDepthAsc(1,"Y");
        return AprvFolderResp.of(folderList);
    }

    // 양식 등록
    // return: true=등록 성공 / false=등록 실패
    @Override
    public Boolean insDocType(AdminDocTypeInsReq docTypeInsReq) {
        try {
            //문서 양식 html 파일 형태로 저장
            AttachFile attachFile = attachFileService.insHtmlFile(docTypeInsReq);
            //현재 문서양식파일 마지막 정렬 순서 번호
            Integer orderValue = documentTypeRepository.countByDocFormUseFlag("Y");

            //entity 생성 및 저장
            docTypeInsReq.setOrderValue(orderValue+1);
            DocumentType documentType = documentTypeRepository.save(docTypeInsReq.of(attachFile));
            return documentType != null;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 양식 수정
    // return: true=수정 성공 / false=수정 실패
    @Override
    public Boolean udtDocType(AdminDocTypeUdtReq adminDocTypeUdtReq) {
        try {
            //기존 파일 flag변경 --> 삭제
            AttachFile deleteFile = attachFileRepository.findById(adminDocTypeUdtReq.getAttachFileNo()).orElseThrow();
            deleteFile.changeFileFlag(false);
            attachFileRepository.save(deleteFile);

            //문서 양식 html 파일 형태로 저장
            AttachFile attachFile = attachFileService.updHtmlFile(adminDocTypeUdtReq);

            //entity 생성 및 저장
            DocumentType documentType = documentTypeRepository.save(adminDocTypeUdtReq.of(attachFile));
            return documentType != null;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public DocFormResp selDocType(Long docFormNo) {

        DocFormResp docFormResp = DocFormResp.toDto(documentTypeRepository.findById(docFormNo).orElseThrow());

        return docFormResp;
    }

    @Override
    public Boolean deleteDocType(Long docFormNo) {
        try {
            // 기존 파일 flag 변경  "Y" -> "N"
            DocumentType documentType = documentTypeRepository.findById(docFormNo).orElseThrow();
            documentType.setDocFormUseFlag("N");
            documentType.setOrderValue(null);
            DocumentType result = documentTypeRepository.save(documentType);

            // 문서양식파일 순서 재정렬
            List<DocumentType> documentTypeList = documentTypeRepository.findByDocFormUseFlagOrderByOrderValue("Y");
            for(int i = 0; i < documentTypeList.size(); i++ ){
                documentTypeList.get(i).setOrderValue(i+1);
                documentTypeRepository.save(documentTypeList.get(i));
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean insDocTypeFolder(DocTypeFolderReq docTypeFolderReq) {
        try {
            // 폴더 정렬 순서 세팅 - depth가 1인 총 개수를 새어 +1한 값을 순서로 세팅
            Integer orderValue = documentTypeFolderRepository.countByDepthAndTypeFolderFlag(1, "Y")+1;
            docTypeFolderReq.setOrderValue(orderValue);

            documentTypeFolderRepository.save(docTypeFolderReq.toEntity());

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean udtDocTypeFolder(UdtFolderReq udtFolderReq) {
        try {
            DocumentTypeFolder folder = documentTypeFolderRepository.findById(udtFolderReq.getFolderNo()).orElseThrow();
            folder.changeTypeFolderName(udtFolderReq.getFolderName());

            documentTypeFolderRepository.save(folder);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 전자결재문서 양식 폴더 삭제
    @Override
    public Boolean delDocTypeFolder(Long docTypeFolderNo) {
        try {
            // 삭제 해당 폴더 데이터 불러옴
            DocumentTypeFolder folder = documentTypeFolderRepository.findById(docTypeFolderNo).orElseThrow();
            // 삭제 폴더 하위 문서양식파일들 삭제 처리
            for(DocumentType documentType : folder.getTypeList()) {
                documentType.setDocFormUseFlag("N");
                documentType.setOrderValue(null);
                documentTypeRepository.save(documentType);
            }
            // 문서양식파일 순서 재정렬
            List<DocumentType> documentTypeList = documentTypeRepository.findByDocFormUseFlagOrderByOrderValue("Y");
            for(int i = 0; i < documentTypeList.size(); i++ ){
                documentTypeList.get(i).setOrderValue(i+1);
                documentTypeRepository.save(documentTypeList.get(i));
            }

            List<DocumentTypeFolder> foldersBelow = documentTypeFolderRepository.findByOrderValueGreaterThanOrderByOrderValueAsc(folder.getOrderValue());


            for (DocumentTypeFolder folderBelow : foldersBelow) {
                Integer orderValue = folderBelow.getOrderValue();
                if (orderValue != null) {
                    folderBelow.changeTypeFolderOrderValue(orderValue - 1);
                    documentTypeFolderRepository.save(folderBelow);
                }
            }

            folder.changeTypeFolderOrderValue(null);
            folder.changeTypeFolderFlag("N");
            documentTypeFolderRepository.save(folder);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // 검색, 페이징
    public Page<DocumentApproval> selDocList(Pageable pageable, DocAprvSearch search, Principal principal) {
        Page<DocumentApproval> docPage = documentApprovalRepository.selAllDocumentApprovals(pageable, search, principal);
        return docPage;
    }

    public Page<DocAprvSelResp> selDocAprvPaging(Pageable pageable, DocAprvSearch search, Principal principal) {
        return DocAprvSelResp.toDto(selDocList(pageable, search, principal));
    }

    @Override
    public Boolean upFolderOrder(Long folderNo) {
        try {
            DocumentTypeFolder selectFolder =  documentTypeFolderRepository.findById(folderNo).orElseThrow();
            DocumentTypeFolder changeFolder = documentTypeFolderRepository.findByOrderValue(selectFolder.getOrderValue()-1);

            changeFolder.changeTypeFolderOrderValue(selectFolder.getOrderValue());
            selectFolder.changeTypeFolderOrderValue(selectFolder.getOrderValue()-1);

            documentTypeFolderRepository.save(changeFolder);
            documentTypeFolderRepository.save(selectFolder);


            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean downFolderOrder(Long folderNo) {
        try {

            DocumentTypeFolder selectFolder =  documentTypeFolderRepository.findById(folderNo).orElseThrow();
            DocumentTypeFolder changeFolder = documentTypeFolderRepository.findByOrderValue(selectFolder.getOrderValue()+1);

            changeFolder.changeTypeFolderOrderValue(selectFolder.getOrderValue());
            selectFolder.changeTypeFolderOrderValue(selectFolder.getOrderValue()+1);

            documentTypeFolderRepository.save(changeFolder);
            documentTypeFolderRepository.save(selectFolder);


            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //전자결재 문서양식 리스트 조회
    @Override
    public List<DocFormListResp> selDocTypeList() {
        List<DocumentType> list = documentTypeRepository.findByDocFormUseFlagOrderByOrderValue("Y");
        return DocFormListResp.of(list);
    }

    @Override
    public Boolean udtDocFormOrder(HashMap<String, Object> paramMap) {

        List<HashMap<String,Object>> data = (List<HashMap<String, Object>>) paramMap.get("data");

        try {
            for (HashMap<String, Object> item : data) {

                String docFormTypeNoStr = (String) item.get("docFormTypeNo");
                Long docFormTypeNo = Long.parseLong(docFormTypeNoStr);

                String orderValueStr = (String) item.get("orderValue");
                Integer orderValue = Integer.parseInt(orderValueStr);

                documentTypeRepository.updateOrderValueByDocFormTypeNo(orderValue, docFormTypeNo);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
