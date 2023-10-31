package com.dflow.approval.controller;

import com.dflow.approval.requestDto.*;
import com.dflow.approval.responseDto.*;
import com.dflow.approval.service.ApprovalService;
import com.dflow.board.responseDto.AttachFileInfo;
import com.dflow.common.dto.ResponseDto;
import com.dflow.common.enumcode.MessageEnu;
import com.dflow.project.responseDto.*;
import com.dflow.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/aprv")
public class ApprovalController {
    private final ProjectService projectService;

    private final ApprovalService approvalService;

    /**
     * 23-8-24 전체 문서 조회, 페이징
     **/
    @GetMapping("/selDocAprv/{docState}")
    public String selApprovalMain(Model model, DocAprvSearch search, Principal principal,
            @PathVariable(value = "docState") String docState,
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "sortType", defaultValue = "desc") String sortType,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @PageableDefault(size = 20, sort = " docNo", direction = Sort.Direction.DESC) Pageable pageable) {
        //memberInfo
        String memberId = principal.getName();
        DocMemberResp memberInfo = approvalService.selMemberInfo(memberId);
        model.addAttribute("memberInfo", memberInfo);

        // state 별로 처리하는 로직 - queryDsl에 로직 추 가후 작업
        // 상태별 DocAprv Type 전달  // 23-8-29 코드 수정  // 23-8-30 principal 추가
        String checkState = approvalService.setDocType(search, docState);
        System.out.println(checkState + "-----------------------" + search.getDocType());
        model.addAttribute("checkState", checkState);


        log.info("----------- 컨트롤러 서치 확인" + search.getKeyword());
        log.info("------------------- 결재일 기안일 확인" + search.getDateType());

        // 구분 리스트 색 들어가는 부분
        List<DocAprvCodeResp> selCodeList = approvalService.selDocAprvCodeList("DocAprv_STATE");
        model.addAttribute("selCodeList", selCodeList);

        log.info(pageSize + "======================== ");

        // 페이징 처리 만질 부분
        Sort sort = sortType.equals("asc") ? Sort.by("docNo") : Sort.by("docNo").descending();
        pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<DocAprvSelResp> docAprvPageList = approvalService.selDocAprvPaging(pageable, search, principal);
        log.info(docAprvPageList + "-----------------------");


        model.addAttribute("docAprvPageList", docAprvPageList);
        model.addAttribute("memberId", memberId);

        RespPage pageInfo = new RespPage(docAprvPageList);
        model.addAttribute("docList", docAprvPageList.getContent()); // 리스트 정보
        model.addAttribute("totalCount", docAprvPageList.getTotalElements()); // 총 데이터 건수
        model.addAttribute("pageInfo", pageInfo); // 페이징 정보
        model.addAttribute("searchList", search);
        model.addAttribute("sortType", sortType); // 정렬 타입(최신순, 과거순)
        model.addAttribute("pageSize", pageSize); // 페이지수

        return "approval/approvalMain";
    }



    // 결재문서 상세조회 모달
    @GetMapping("/selDocDetail/{docNo}")
    public ResponseEntity<?> selDocDetail(@PathVariable Long docNo){

        System.out.println("------------------연결됨-----------------------");
        System.out.println(docNo);
        System.out.println("------------------연결됨-----------------------");

        DocAprvSelResp docAprv = approvalService.selDocDetail(docNo);
        docAprv.setFileNo(approvalService.selDocFileNoList(docNo));
        String responseMsg = MessageEnu.valueOf(MessageEnu.OK.name()).getTitle();
        if (docAprv.equals("")) {
            responseMsg = MessageEnu.valueOf(MessageEnu.NO_DATA.name()).getTitle();
        }
        ResponseDto responseDto = ResponseDto.builder()
                .code("200")
                .msg(responseMsg)
                .data(docAprv)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //결재 요청
    @PostMapping("/udtDocAprvState")
    public ResponseEntity<?> udtDocAprvState(@ModelAttribute AprvUdtReq req, Principal principal) {

        Boolean result = approvalService.udtAprv(req);
        String responseMsg;

        if(result)
            responseMsg = req.getAprvResult() + "하였습니다.";
        else
            responseMsg = req.getAprvResult() + "에 실패하였습니다.";

        ResponseDto responseDto = ResponseDto.builder()
                .code("200")
                .msg(responseMsg)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    //회수 요청
    @PutMapping("/udtWithdrawalAprv/{docNo}")
    public ResponseEntity<?> udtWithdrawalAprv(@PathVariable Long docNo) {
        log.info("------------------------연결 완료");
        Boolean result = approvalService.udtWithdrawalAprv(docNo);
        String responseMsg = result ? "문서를 회수하였습니다." : "문서 회수에 실패하였습니다.";
        ResponseDto responseDto = ResponseDto.builder()
                .code("200")
                .msg(responseMsg)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 전자결재 - 신규작성 페이지
    @GetMapping("/aprvReg")
    public String regApproval(Model model, Principal principal) {
        String memberId = principal.getName();
        DocMemberResp memberInfo = approvalService.selMemberInfo(memberId);
        model.addAttribute("memberInfo", memberInfo);

        List<DocTypeResp> docTypeList = approvalService.selDocTypeList();
        model.addAttribute("docTypeList", docTypeList);

        List<RespProjDept> departmentList = projectService.selResourceDepartmentList();
        model.addAttribute("departmentList", departmentList);
        return "approval/approvalRegister";
    }

    // 전자결재 - 신규작성 - 양식 요청
    @GetMapping("/selAprvRegDocTypeDetail/{docFormTypeNo}")
    public ResponseEntity<HashMap<String, Object>> selAprvRegDocTypeDetail(
            @PathVariable("docFormTypeNo") Long docFormTypeNo) {
        DocTypeDetailResp doctype = approvalService.selDocTypeDetail(docFormTypeNo);
        HashMap<String, Object> map = new HashMap<>();
        if(doctype.getDocFormCode().equals("ANU")) {
            List<RespProjCode> typeCodes = approvalService.selAnnualCodeType();
            map.put("typeCodes", typeCodes);
        }
        map.put("doctype", doctype);
        map.put("msg", "전송 성공");
        return ResponseEntity.ok(map);
    }

    // 전자결재 - 신규작성 - 문서등록
    @PostMapping("/insDocAprvMain")
    public ResponseEntity<HashMap<String, Object>> insdocaprvMain(@ModelAttribute DocAprvInsReq req,
                                                                  @RequestParam(value = "files", required = false) MultipartFile[] uploadFile,
                                                                   Principal principal) {
        String memberId = principal.getName();
        HashMap<String, Object> map = new HashMap<>();
        Boolean isSaved = approvalService.insDocAprv(req, uploadFile, memberId);
        if (isSaved) {
            map.put("msg", "문서를 등록하였습니다.");
            return ResponseEntity.ok(map);
        } else {
            map.put("msg", "전송 실패");
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //전자결재 - 신규작성 - 임시저장문서 -> 결재문서등록
    @PostMapping("/insDocAprvTemp")
    public ResponseEntity<?> insDocAprvTemp(@ModelAttribute DocAprvInsReq req,
                                            @RequestParam Long docNo,
                                            @RequestParam(value = "files", required = false) MultipartFile[] uploadFile,
                                            Principal principal){

        String memberId = principal.getName();
        HashMap<String, Object> map = new HashMap<>();
        DocAprvSelResp result =  approvalService.delDocAprv(docNo);
        Boolean isSaved = approvalService.insDocAprv(req, uploadFile, memberId);

        if (isSaved) {
            map.put("msg", "문서를 등록하였습니다.");
            return ResponseEntity.ok(map);
        } else {
            map.put("msg", "전송 실패");
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //전자결재 - 신규작성 - 회수문서수정 -> 문서 수정 등록
    @PostMapping("/insDocAprvUdt")
    public ResponseEntity<?> insDocAprvUdt(@ModelAttribute DocAprvInsReq req,
                                            @RequestParam Long docNo,
                                            @RequestParam(value = "files", required = false) MultipartFile[] uploadFile,
                                            @RequestParam(value = "delFiles", required = false) List<Long> delFiles,
                                            Principal principal){
        String memberId = principal.getName();
        HashMap<String, Object> map = new HashMap<>();
        Boolean insDoc = approvalService.udtDocAprv(req, docNo);
        Boolean insFile = approvalService.fileUpload(uploadFile, docNo, memberId);
            Boolean delFile = approvalService.delFiles(delFiles);
        if (insDoc && insFile && delFile) {
            map.put("msg", "문서를 수정하였습니다.");
            return ResponseEntity.ok(map);
        } else {
            map.put("msg", "전송 실패");
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 전자결재 - 신규작성 - 임시저장
    @PostMapping("/insTempDocAprv")
    public ResponseEntity<?> insTempDocAprv(@ModelAttribute TempDocAprvReq req) {

        HashMap<String, Object> map = new HashMap<>();
        Boolean isSaved = approvalService.insTempDocAprv(req);
        if (isSaved) {
            map.put("msg", "임시저장하였습니다.");
            return ResponseEntity.ok(map);
        } else {
            map.put("msg", "전송 실패");
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 전자결재 - 임시저장 - 임시저장 수정
    @PutMapping("/udtTempDocAprv")
    public ResponseEntity<?> udtTempDocAprv(@ModelAttribute TempDocAprvReq req) {

        HashMap<String, Object> map = new HashMap<>();
        Boolean isUdt = approvalService.udtTempDocAprv(req);
        if (isUdt) {
            map.put("msg", "임시수정하였습니다.");
            return ResponseEntity.ok(map);
        } else {
            map.put("msg", "전송 실패");
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/aprvTempMain")
    public String selAprvTempMain(Model model, Principal principal, TempDocAprvPageReq req) {
        //req.changePage(page);
        log.info("page : "+req.getPage());
        log.info("direction : "+req.getDirection());
        //req.changeSize(size);
        String memberId = principal.getName();
        log.info("memberId : "+memberId);
        log.info("req : "+req);
        TempDocAprvResp<TempDocAprvInfoResp> tempDocAprvList = approvalService.tempPageList(req, memberId);
        

        model.addAttribute("tempDocAprvList", tempDocAprvList);

        return "approval/approvalTempMain";
    }

    @GetMapping("/aprvSign")
    public String approvalSign(Principal principal, Model model) {

        String memberId = principal.getName();
        DocMemberResp memberInfo = approvalService.selMemberInfo(memberId);

        model.addAttribute("signNo", memberInfo.getSignNo());

        return "approval/approvalSign";
    }

    // 서명 저장
    @PostMapping("/insSignMain")
    public ResponseEntity<String> insAprvSign(Principal principal,
            @RequestParam("signImg") MultipartFile signImg) {

        String memberId = principal.getName();

        boolean isSignSaved = approvalService.saveSignToMember(memberId, signImg);

        if (isSignSaved) {
            return ResponseEntity.ok("서명이 등록되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서명 등록에 실패하였습니다.");
        }
    }

    // 서명 삭제
    @DeleteMapping("/delSignMain")
    public ResponseEntity<String> delAprvSign(Principal principal) {

        String memberId = principal.getName();

        boolean isDeleted = approvalService.deleteSignToMember(memberId);

        if (isDeleted) {
            return ResponseEntity.ok("서명이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서명 삭제 중 오류가 발생했습니다.");
        }
    }
    // 임시저장문서 재작성
    @GetMapping("/aprvReg/temp/{docAprvNo}")
    public String tempAprvReg(Model model, @PathVariable("docAprvNo") Long docNo, Principal principal){

        TempDocAprvDetailResp tempDocAprv = approvalService.selTempDocAprv(docNo);

        String memberId = principal.getName();
        DocMemberResp memberInfo = approvalService.selMemberInfo(memberId);

        List<RespProjDept> departmentList = projectService.selResourceDepartmentList();

        model.addAttribute("type","temp");
        model.addAttribute("docAprv", tempDocAprv);
        model.addAttribute("memberInfo", memberInfo);
        model.addAttribute("departmentList", departmentList);


        return"approval/approvalUpdate";
    }

    // 회수문서 수정 후 재등록
    @GetMapping("/aprvReg/udt/{docAprvNo}")
    public String udtAprvReg(Model model,@PathVariable("docAprvNo") Long docNo, Principal principal){

        TempDocAprvDetailResp tempDocAprv = approvalService.selTempDocAprv(docNo);


        String memberId = principal.getName();
        DocMemberResp memberInfo = approvalService.selMemberInfo(memberId);


        List<RespProjDept> departmentList = projectService.selResourceDepartmentList();


        model.addAttribute("type","udt");
        model.addAttribute("docAprv", tempDocAprv);
        model.addAttribute("memberInfo", memberInfo);
        model.addAttribute("departmentList", departmentList);


        return"approval/approvalUpdate";
    }

    @GetMapping("/selFileList/{docNo}")
    public String selFileList(Model model, @PathVariable("docNo") Long docNo){
        List<AttachFileInfo> fileList = approvalService.selAttachFileList(docNo);
        model.addAttribute("fileList", fileList);

        return "approval/more/upload_box";
    }




    /**
     *  23-9-1 회수 후 삭제 구현
     *
     */

    @DeleteMapping("/selDocAprv/cancel/{docNo}")
    public ResponseEntity<?> returnDelete(@PathVariable("docNo")Long docNo){

        DocAprvSelResp docAprv = approvalService.delDocAprv(docNo);

        String responseMsg = MessageEnu.valueOf(MessageEnu.OK.name()).getTitle();

        ResponseDto responseDto = ResponseDto.builder()
                .code("200")
                .msg(responseMsg)
                .data(docAprv)
                .build();

        if (docAprv == null || docAprv.getDocFlag().equals("Y")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    /**
     * 23-9-1 임시 저장 문서 삭제 구현
     * */
    @DeleteMapping("/aprvTempMain/{docAprvNo}")
    public ResponseEntity<?> tempDelete(@PathVariable("docAprvNo")Long docAprvNo){

        log.info(docAprvNo  + " ++++++++++++");

        DocAprvSelResp docAprv = approvalService.delDocAprv(docAprvNo);

        String responseMsg = MessageEnu.valueOf(MessageEnu.OK.name()).getTitle();

        ResponseDto responseDto = ResponseDto.builder()
                .code("200")
                .msg(responseMsg)
                .data(docAprv)
                .build();

        if (docAprv == null || docAprv.getDocFlag().equals("Y")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }


}
