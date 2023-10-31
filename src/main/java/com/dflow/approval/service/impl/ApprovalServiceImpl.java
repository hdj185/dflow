package com.dflow.approval.service.impl;

import com.dflow.approval.requestDto.*;
import com.dflow.approval.responseDto.*;
import com.dflow.approval.service.ApprovalService;
import com.dflow.attachFile.requestDto.ReqFile;
import com.dflow.attachFile.service.AttachFileService;
import com.dflow.board.responseDto.AttachFileInfo;
import com.dflow.entity.*;
import com.dflow.project.responseDto.RespProjCode;
import com.dflow.repository.*;
import com.dflow.repository.AttachFileRepository;
import com.dflow.repository.DocumentApprovalRepository;
import com.dflow.repository.DocumentTypeRepository;
import com.dflow.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.security.Principal;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class  ApprovalServiceImpl implements ApprovalService {

    private final DocumentApprovalRepository documentApprovalRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final ApprovalRepository approvalRepository;
    private final AttachFileRepository attachFileRepository;
    private final AttachFileService attachFileService;
    private final MemberInfoRepository memberInfoRepository;
    private final CodeInfoRepository codeInfoRepository;
    private final ReferenceRepository referenceRepository;
    private final TempDocAprvInfoResp.TempDocAprvInfoMapper tempDocAprvInfoMapper;
    private final ReferenceFileRepository referenceFileRepository;
    private final AnnualLeaveRepository annualLeaveRepository;
    private final LeaveRecordRepository leaveRecordRepository;

    @Value("${file.uploadDir}")
    private String uploadPath;


    /**
     * 23-8-24 문서 전체 조회
     **/
    public List<DocAprvSelResp> docAprvSelAllList() {

        List<DocAprvSelResp> docList2 = documentApprovalRepository
                .findAllByDocFlag("Y")
                .stream()
                .map(DocAprvSelResp::toDto)
                .collect(Collectors.toList());

        return docList2;
    }

    // 전자결재 문서 상세조회
    public DocAprvSelResp selDocDetail(Long docNo) {
        Optional<DocumentApproval> docAprv = documentApprovalRepository.findById(docNo);

        if (!docAprv.isPresent()) return null;
        DocumentApproval entity = docAprv.get();

        return DocAprvSelResp.toDto(entity);
    }

    // 전자결재 문서 삭제
    public DocAprvSelResp delDocAprv(Long docNo) {
        try {
            Optional<DocumentApproval> opDocAprv = documentApprovalRepository.findById(docNo);
            if (opDocAprv.isPresent()) {
                DocumentApproval docAprv = opDocAprv.get();
                docAprv.setDocFlag("N");
                return DocAprvSelResp.toDto(documentApprovalRepository.save(docAprv));
            } return null;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //전자결재 문서양식 리스트 조회
    @Override
    public List<DocTypeResp> selDocTypeList() {
        List<DocumentType> list = documentTypeRepository.findByDocFormUseFlagOrderByOrderValue("Y");
        return DocTypeResp.of(list);
    }

    //연차 코드 리스트 조회
    @Override
    public List<RespProjCode> selAnnualCodeType() {
        List<CodeInfo> codeInfoList = codeInfoRepository.findCodeInfoByCodeTypeAndCodeFlagFalse("ANNUAL_TYPE");
        return RespProjCode.of(codeInfoList);
    }

    //전자결재 문서양식 상세 조회
    @Override
    public DocTypeDetailResp selDocTypeDetail(Long docFormTypeNo) {
        Optional<DocumentType> optional = documentTypeRepository.findById(docFormTypeNo);
        if (!optional.isPresent()) return null;

        //파일 정보로 html 파일 내용 찾기
        DocumentType entity = optional.get();
        String contents = attachFileService.readHtmlFileToString(entity.getFileAttachNo());

        //dto로 변환
        return DocTypeDetailResp.of(entity, contents);
    }

    //전자결재 - 문서 신규 작성
    @Override
    public Boolean insDocAprv(DocAprvInsReq docReq, MultipartFile[] uploadFile, String memberName) {

        try {
            MemberInfo member = memberInfoRepository.findByMemberId(memberName)
                    .orElseThrow(() -> new NoSuchElementException("멤버 정보를 찾을 수 없습니다: " + memberName));
            //문서 정보 저장
            DocumentApproval document = documentApprovalRepository.save(docReq.toDocEntity());

            //결재라인 정보 저장
            List<Approval> approvalList = docReq.toAprvEntity(document.getDocNo());
            approvalRepository.saveAll(approvalList);

            //참조라인 정보 저장
            List<Reference> referenceList = docReq.toRefEntity(document.getDocNo());
            referenceRepository.saveAll(referenceList);

            if (uploadFile != null && uploadFile.length > 0) {  // 파일이 있을 경우

                for (MultipartFile file : uploadFile) {
                    // 파일이 비어있을 경우 건너뛴다.
                    if (file.isEmpty()) {
                        continue;
                    }

                    ReqFile reqFile = new ReqFile(file, uploadPath);

                    // 실제 파일 저장
                    file.transferTo(new File(reqFile.getFileLocation()));

                    // 데이터베이스에 파일 정보 저장
                    AttachFile attachFile = reqFile.toEntity();
                    ReferenceFileDocReq fileDto = new ReferenceFileDocReq(document.getDocNo(), attachFileRepository.save(attachFile).getFileAttachNo(), member);
                    referenceFileRepository.save(fileDto.toEntitiy());
                }

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  전자결재문서 - 임시저장
     * **/
    @Override
    public Boolean insTempDocAprv(TempDocAprvReq tempDocAprvReq) {

        try {

            //문서 정보 저장
            DocumentApproval document = documentApprovalRepository.save(tempDocAprvReq.toEntity());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  전자결재문서 - 임시저장 수정
     * **/
    @Override
    public Boolean udtTempDocAprv(TempDocAprvReq tempDocAprvReq) {
        try {
            //문서 불로오기
            DocumentApproval documentApproval = documentApprovalRepository.findById(tempDocAprvReq.getDocNo()).orElseThrow();
            //문서 정보 저장
            documentApproval.changeDocTTL(tempDocAprvReq.getDocTTL());
            documentApproval.changeDocCn(tempDocAprvReq.getDocCn());
            documentApproval.changeDocFormNo(tempDocAprvReq.getDocFormNo());


            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 전자결재문서 - 회수 문서 수정 후 재등록
     * **/
    @Override
    public Boolean udtDocAprv(DocAprvInsReq req, Long docNo) {
        try {
            //문서 내용 수정
//            Approval approval =  approvalRepository.findApprovalByDocumentNo(docNo, 1).orElseThrow();
//            approval.setAprvResult("진행중");
//            approval.setAprvCheck("Y");
//            approvalRepository.save(approval);
            //결재라인 정보 저장
            List<Approval> approvalList = req.toAprvEntity(docNo);
            approvalRepository.saveAll(approvalList);

            //참조라인 정보 저장
            List<Reference> referenceList = req.toRefEntity(docNo);
            referenceRepository.saveAll(referenceList);

            DocumentApproval documentApproval = documentApprovalRepository.findById(docNo).orElseThrow();
            documentApproval.setDocCn(req.getDocCn());
            documentApproval.setDocState("대기");
            documentApprovalRepository.save(documentApproval);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 전자결재문서 파일 첨부 업로드
     * **/
    @Override
    public Boolean fileUpload(MultipartFile[] uploadFile, Long docNo, String memberId) {
        try {
            MemberInfo member = memberInfoRepository.findByMemberId(memberId)
                    .orElseThrow(() -> new NoSuchElementException("멤버 정보를 찾을 수 없습니다: " + memberId));

            if (uploadFile != null && uploadFile.length > 0) {  // 파일이 있을 경우

                for (MultipartFile file : uploadFile) {
                    // 파일이 비어있을 경우 건너뛴다.
                    if (file.isEmpty()) {
                        continue;
                    }

                    ReqFile reqFile = new ReqFile(file, uploadPath);

                    // 실제 파일 저장
                    file.transferTo(new File(reqFile.getFileLocation()));

                    // 데이터베이스에 파일 정보 저장
                    AttachFile attachFile = reqFile.toEntity();
                    ReferenceFileDocReq fileDto = new ReferenceFileDocReq(docNo, attachFileRepository.save(attachFile).getFileAttachNo(), member);
                    referenceFileRepository.save(fileDto.toEntitiy());
                }

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 전자결재문서 수정 - 첨부파일 삭제
     * **/
    @Override
    public Boolean delFiles(List<Long> delFiles) {
        try {
            // 참조 파일 flag 변경
            if(delFiles.size() > 0){
                for(Long no : delFiles){
                    ReferenceFile referenceFile = referenceFileRepository.findById(no).orElseThrow();
                    referenceFile.changeFileFlag(true);
                    referenceFileRepository.save(referenceFile);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //회원 이름/부서/직책 정보 받아오기
    @Override
    public DocMemberResp selMemberInfo(String memberId) {
        return DocMemberResp.of(memberInfoRepository.findByMemberId(memberId).get());
    }

    @Override
    /**
     * 23-8-25 코드 구분 리스트 구하기
     **/
    public List<DocAprvCodeResp> selDocAprvCodeList(String codeType) {
        List<CodeInfo> codeInfoList = codeInfoRepository.findCodeInfoByCodeTypeAndCodeFlagFalse(codeType);

        return DocAprvCodeResp.toDto(codeInfoList);
    }

    // 결재 처리
    @Override
    public Boolean udtAprv(AprvUdtReq req) {
        try{
            //docNo에 해당하는 결재 정보 리스트 형태로 불러오기
            List<Approval> approvalList = approvalRepository.findApprovalsByDocumentNo(req.getDocNo());
            Optional<DocumentApproval> optional = documentApprovalRepository.findById(req.getDocNo());
            if(optional.isEmpty() || optional.get().getDocState().equals("회수")) return false;   //문서가 없거나 회수되었다면 return false
            DocumentApproval document = optional.get();

            //멤버 id가 일치하는 apprval 찾기
            for(int i = 0; i < approvalList.size(); i++) {
                Approval approval = approvalList.get(i);
                if(req.getMemberNo() == approval.getAprvMemberNo()) {
                    approval.setAprvCheck("N");
                    approval.setAprvTime(LocalDateTime.now());
                    approval.setAprvOpinion(req.getAprvOpinion());
                    approval.setAprvResult(req.getAprvResult());
                    approvalRepository.save(approval);

                    document.setDocCn(req.getAprvContent());

                    // 승인인 경우 결재선 마지막인지 아닌지 확인
                    if(req.getAprvResult().equals("승인") && i + 1 < approvalList.size()) {
                        //마지막 아니면 다음 라인 진행시키기(check = "Y"로 변환/result = "진행중"으로 변경)
                        Approval nextApproval = approvalList.get(i + 1);
                        nextApproval.setAprvCheck("Y");
                        nextApproval.setAprvResult("진행중");
                        approvalRepository.save(nextApproval);
                        document.setDocState("진행중");
                    } else {
                        //마지막이거나 반려일 시, 문서 상태 변경
                        if(req.getAprvResult().equals("승인")) {
                            //연차 신청서일 경우
                            if(document.getDocTTL().split("-")[0].equals("ANU")) {
                                insLeaveReacord(document);
                            }
                            document.setDocState("완료");
                        } else {
                            document.setDocState("반려");
                        }
                    }
                    documentApprovalRepository.save(document);
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //연차 신청서 결재 시 - 연차 사용 기록 저장
    @Override
    public Boolean insLeaveReacord(DocumentApproval document) {
        // Jsoup을 사용하여 HTML 파싱
        try {
            Document doc = Jsoup.parse(document.getDocCn());    //파싱 준비
            //문서에서 값 받아오기
            LocalDate startDate = LocalDate.parse(getSpanTxt(doc, "annualStartDate"));  //연차 시작일
            LocalDate endDate = LocalDate.parse(getSpanTxt(doc, "annualEndDate"));      //연차 종료일
            String annualType = getSpanTxt(doc, "annualType");  //연차 타입

            Double useCount = 0d;   //연차 사용일
            //반차&반반차인 경우와 그외 처리(입력받은 기간 계산)
            switch (annualType) {
                case "반차":
                    useCount = 0.5;
                    break;
                case "반반차":
                    useCount = 0.25;
                    break;
                default:
                    useCount = ChronoUnit.DAYS.between(startDate, endDate) + 1.0;
            }

            //연차 기록 저장
            LeaveRecord record = LeaveRecord.builder()
                    .leaveType(annualType)
                    .leaveContents(getSpanTxt(doc, "annualReason"))
                    .leaveStartDate(startDate)
                    .leaveEndDate(endDate)
                    .leaveUseCount(useCount)
                    .member(document.getMemberInfo())
                    .document(document)
                    .build();
            leaveRecordRepository.save(record);

            //연차 정보에서 사용 연차 차감
            AnnualLeave annualLeave = annualLeaveRepository.findAnnualLeaveByMemberId(document.getMemberInfo().getMemberId()).get();
            Double leftCount = annualLeave.getAnnualLeft() - useCount;
            annualLeave.setAnnualLeft(leftCount);
            annualLeaveRepository.save(annualLeave);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //span에서 text값 구하기
    @Override
    public String getSpanTxt(Document doc, String id) {
        Element spanElement = doc.select("span#" + id).first();
        if(spanElement != null)
            return spanElement.text();
        else
            return null;
    }

    // 전자결재 문서 회수 - 반환값: true = 회수 성공 / false = 회수 실패
    @Override
    public Boolean udtWithdrawalAprv(Long docNo) {
        try {
            Optional<DocumentApproval> docOptional = documentApprovalRepository.findById(docNo);
            if(!docOptional.isPresent()) return false; //값이 없으면 false return
            DocumentApproval document = docOptional.get();
            if(!document.getDocState().equals("대기"))    return false;   //문서가 대기상태가 아니면 false return

            //결재라인 삭제
            List<Approval> approvalList = approvalRepository.findApprovalsByDocumentNo(docNo);
            approvalRepository.deleteAllInBatch(approvalList);

            //참조라인 삭제
            List<Reference> referenceList = referenceRepository.findReferenceByDocNo(docNo);
            referenceRepository.deleteAllInBatch(referenceList);

            //문서 정보 회수로 변경
            document.setDocState("회수");
            documentApprovalRepository.save(document);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Long> selDocFileNoList(Long docNo) {
        List<ReferenceFile> referenceFileList = referenceFileRepository.findAllByDocAprvNoAndFileFlag(docNo, false);
        List<Long> list = new ArrayList<>();
        for(ReferenceFile entity : referenceFileList)
            list.add(entity.getAttachFileNo());
        return list;
    }

    @Override
    public List<AttachFileInfo> selAttachFileList(Long docNo) {

        List<AttachFileInfo> fileList = referenceFileRepository.findAllByDocAprvNoAndFileFlag(docNo, false)
                .stream()
                .map(AttachFileInfo::toDto)
                .collect(Collectors.toList());

        return fileList;
    }


    /**
    *  23-8-27 페이징
    *  23-8-30 접속자가  기안자거나 참조일 때만 문서 조회 기능 추가
    *
    * **/
   public Page<DocumentApproval> selDocList(Pageable pageable, DocAprvSearch search, Principal principal){
       Page<DocumentApproval> docPage = documentApprovalRepository.selDocumentApprovalsByConditions(pageable, search, principal);
           return docPage;
   }

   public Page<DocAprvSelResp> selDocAprvPaging(Pageable pageable, DocAprvSearch search, Principal principal) {
       return DocAprvSelResp.toDto(selDocList(pageable, search, principal));
   }

    /**
     * 임시 저장 페이징 리스트
     * **/
    @Override
    public TempDocAprvResp<TempDocAprvInfoResp> tempPageList(TempDocAprvPageReq req, String memberId) {
       MemberInfo memberInfo = memberInfoRepository.findByMemberId(memberId).orElseThrow();
       Pageable pageable = req.getPageable("docNo");
       Page<DocumentApproval> result = documentApprovalRepository.selTempDocumentApproval(pageable, memberInfo.getMemberNo());

       List<TempDocAprvInfoResp> list = result.getContent().stream().map(documentApproval -> tempDocAprvInfoMapper.apply(documentApproval)).collect(Collectors.toList());
       return TempDocAprvResp.<TempDocAprvInfoResp>withAll()
               .tempDocAprvPageReq(req)
               .pageResponseList(list)
               .total((int)result.getTotalElements())
               .build();
    }

    /**
    *  23-8-28 검색, 페이징 , 주소별 state 세팅
    * **/

   public  String selDocState(DocAprvSearch search, String docState){

//       // 기안자가 현재 접속한 사용자와 다르고, 그 문서의 상태가 "대기"인 경우
//       if(!memberId.equals(allListMemberId) && docState.equals("pending")) {
//           docState = "결재대기"; // 결재대기 상태를 표현하는 적절한 문자열로 대체해주세요.
//       }

       if(docState.equals("progress"))
           search.setDocState("진행중");

       else if(docState.equals("pending"))
           search.setDocState("대기");

       else if(docState.equals("cancel"))
           search.setDocState("반려/회수");

       else if(docState.equals("completed"))
           search.setDocState("완료");

       else if(docState.equals("ref"))
           search.setDocState("참조");

       else
           search.setDocState(null);

       return docState;
   }

   /** 23-8-29 네비게이션 바 추가  수정 **/
   public String setDocType(DocAprvSearch search, String docState){
      String setType =  selDocState(search, docState);
           if(setType != null ){
               if(setType.equals("main"))
                   search.setDocType("전체문서"); 
               else if (setType.equals("progress"))
                   search.setDocType("진행문서");
               else if (setType.equals("completed"))
                   search.setDocType("완료문서");
               else if (setType.equals("cancel"))
                   search.setDocType("반려/회수문서");
               else if (setType.equals("pending"))
                   search.setDocType("대기문서");
               else if(setType.equals("ref"))
                   search.setDocType("참조문서");


           }
           return setType;
   }

   /**
    * 임시저장문서 상세 정보 조회
    * **/
    @Override
    public TempDocAprvDetailResp selTempDocAprv(Long docNo) {

       TempDocAprvDetailResp result = TempDocAprvDetailResp.toDto(documentApprovalRepository.findById(docNo).orElseThrow());

       return result;
    }

//   /** 23-8-30 접속 자가 기안자 일 때 **/
//   public String docAprvCheckMe(Principal principal, ){
//
//   }

    // 서명 저장
    public boolean saveSignToMember(String memberId, MultipartFile signImg) {

        MemberInfo member = memberInfoRepository.findByMemberId(memberId).get();

        if (signImg != null && !signImg.isEmpty()) {
            Long imgNo = attachFileService.insFile(signImg).getFileAttachNo();
            member.changeSignAttachNo(imgNo);
        } else {
            member.changeSignAttachNo(null);
        }

        try {
            memberInfoRepository.save(member);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 서명 삭제
    @Override
    public boolean deleteSignToMember(String memberId) {

        MemberInfo member = memberInfoRepository.findByMemberId(memberId).get();

        member.changeSignAttachNo(null);

        try {
            memberInfoRepository.save(member);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
