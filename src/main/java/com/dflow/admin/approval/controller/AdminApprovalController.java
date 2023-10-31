package com.dflow.admin.approval.controller;

import com.dflow.admin.approval.requestDto.AdminDocTypeInsReq;
import com.dflow.admin.approval.requestDto.AdminDocTypeUdtReq;
import com.dflow.admin.approval.requestDto.DocTypeFolderReq;
import com.dflow.admin.approval.requestDto.UdtFolderReq;
import com.dflow.admin.approval.responseDto.*;
import com.dflow.admin.approval.service.AdminApprovalService;
import com.dflow.approval.requestDto.DocAprvSearch;
import com.dflow.approval.responseDto.DocAprvCodeResp;
import com.dflow.approval.responseDto.DocAprvSelResp;
import com.dflow.approval.responseDto.DocMemberResp;
import com.dflow.approval.service.ApprovalService;
import com.dflow.attachFile.service.AttachFileService;
import com.dflow.common.dto.ResponseDto;
import com.dflow.common.enumcode.MessageEnu;
import com.dflow.project.responseDto.RespPage;
import lombok.Getter;
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

import java.security.Principal;
import java.util.HashMap;
import java.util.List;


@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin/aprv")
public class AdminApprovalController {

    private final AdminApprovalService adminApprovalService;
    private final ApprovalService approvalService;
    private final AttachFileService attachFileService;

    // 전자결재관리 불러오기
    @GetMapping("/aprvMgt")
    public String selApprovalMgt(Model model, DocAprvSearch search, Principal principal,
                                 @RequestParam(defaultValue = "1", required = false) Integer page,
                                 @RequestParam(name = "sortType", defaultValue = "desc") String sortType,
                                 @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                 @PageableDefault(size = 20, sort = " docNo", direction = Sort.Direction.DESC) Pageable pageable) {

        //memberInfo
        String memberId = principal.getName();
        DocMemberResp memberInfo = adminApprovalService.selAdminInfo(memberId);

        model.addAttribute("memberInfo", memberInfo);

        // 구분 리스트 색 들어가는 부분
        List<DocAprvCodeResp> selCodeList = approvalService.selDocAprvCodeList("DocAprv_STATE");
        model.addAttribute("selCodeList", selCodeList);

        log.info(pageSize + "======================== ");

        // 페이징 처리 만질 부분
        Sort sort = sortType.equals("asc") ? Sort.by("docNo") : Sort.by("docNo").descending();
        pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<DocAprvSelResp> docAprvPageList = adminApprovalService.selDocAprvPaging(pageable, search, principal);
        log.info(docAprvPageList + "-----------------------");


        model.addAttribute("docAprvPageList", docAprvPageList);

        RespPage pageInfo = new RespPage(docAprvPageList);
        model.addAttribute("docList", docAprvPageList.getContent()); // 리스트 정보
        model.addAttribute("totalCount", docAprvPageList.getTotalElements()); // 총 데이터 건수
        model.addAttribute("pageInfo", pageInfo); // 페이징 정보
        model.addAttribute("searchList", search);
        model.addAttribute("sortType", sortType); // 정렬 타입(최신순, 과거순)
        model.addAttribute("pageSize", pageSize); // 페이지수

        return "admin/approval/admin_approvalMgt";
    }

    // 결재문서 상세조회 모달
    @GetMapping("/selDocDetail/{docNo}")
    public ResponseEntity<?> selDocDetail(@PathVariable Long docNo){

        System.out.println("------------------연결됨-----------------------");
        System.out.println(docNo);
        System.out.println("------------------연결됨-----------------------");

        DocAprvSelResp docAprv = approvalService.selDocDetail(docNo);
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

    // 결재문서 삭제
//    @DeleteMapping("delDocAprv/{docNo}")
//    public ResponseEntity<?> delDocAprv(@PathVariable Long docNo) {
//
//        DocAprvSelResp docAprv = approvalService.delDocAprv(docNo);
//
//        String responseMsg = MessageEnu.valueOf(MessageEnu.OK.name()).getTitle();
//        ResponseDto responseDto = null;
//
//        if (docAprv.getDocFlag().equals("N")) {
//            responseDto = ResponseDto.builder()
//                    .code("200")
//                    .msg(MessageEnu.valueOf(MessageEnu.OK.name()).getTitle())
//                    .data(docAprv)
//                    .build();
//        } else {
//            responseDto = ResponseDto.builder()
//                    .code("200")
//                    .msg(MessageEnu.valueOf(MessageEnu.OK.name()).getTitle())
//                    .data(docAprv)
//                    .build();
//        }
//
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }

    @DeleteMapping("/delDocAprv/{docNo}")
    public ResponseEntity<?> delDocAprv(@PathVariable Long docNo) {

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

    // 문서양식관리 불러오기
    @GetMapping("/aprvType")
    public String selApprovalType(Model model, Principal principal) {
        List<AprvCodeResp> ynCodes = adminApprovalService.selAprvCodeList();
        List<AprvFolderResp> aprvFolders = adminApprovalService.selAprvFolderList();
        List<DocFolderTreeResp> folderTreeRespList = adminApprovalService.getFolderDeptNo();
        DocMemberResp memberInfo = approvalService.selMemberInfo(principal.getName());

        HashMap<String, Object> urlList = new HashMap<String, Object>();
        urlList.put("selFormUrl","/admin/aprv/selDocType");
        urlList.put("udtFormUrl","/admin/aprv/udtDocTypeMain");
        urlList.put("deleteFormUrl","/admin/aprv/deleteDocType");
        urlList.put("addFolderUrl","/admin/aprv/insDocTypeFolder");
        urlList.put("udtFolderUrl","/admin/aprv/udtDocTypeFolder");
        urlList.put("selFolderUrl","/admin/aprv/selDocTypeFolder");

        model.addAttribute("urlList", urlList);
        model.addAttribute("folderTreeRespList", folderTreeRespList);
        model.addAttribute("ynCodes", ynCodes); // 사용 여부
        model.addAttribute("aprvFolders", aprvFolders);
        model.addAttribute("memberInfo", memberInfo);   //사용자 정보

        log.info(aprvFolders);

        return "admin/approval/admin_approvalType";
    }


    // 문서 양식 관리 - 문서 양식 등록 요청
    @PostMapping("/insDocTypeMain")
    public ResponseEntity<?> insDocTypeMain(@RequestBody AdminDocTypeInsReq req) {
        System.out.println("컨트롤러 컨텐츠 = " + req.getContents());
        System.out.println("컨트롤러 name = " + req.getDocFormName());
        boolean result = adminApprovalService.insDocType(req);
        String responseMsg;
        ResponseDto responseDto;
        if (result) {
            responseMsg = MessageEnu.valueOf(MessageEnu.SAVE_OK.name()).getTitle();
            responseDto = ResponseDto.builder()
                    .code("200")
                    .msg(responseMsg)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);

        } else {
            responseMsg = MessageEnu.valueOf(MessageEnu.NO_EXECUTE.name()).getTitle();
            responseDto = ResponseDto.builder()
                    .code("500")
                    .msg(responseMsg)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);

        }
    }

    //문서 양식 관리 - 문서 양식 수정 요청
    @PostMapping("/udtDocTypeMain")
    public ResponseEntity<?> udtDocTypeMain(@RequestBody AdminDocTypeUdtReq req){
        log.info("컨트롤러 컨텐츠 = " + req.getContents());
        log.info("컨트롤러 name = " + req.getDocFormName());

        boolean result = adminApprovalService.udtDocType(req);
        String responseMsg;
        ResponseDto responseDto;
        if (result) {
            responseMsg = MessageEnu.valueOf(MessageEnu.SAVE_OK.name()).getTitle();
            responseDto = ResponseDto.builder()
                    .code("200")
                    .msg(responseMsg)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);

        } else {
            responseMsg = MessageEnu.valueOf(MessageEnu.NO_EXECUTE.name()).getTitle();
            responseDto = ResponseDto.builder()
                    .code("500")
                    .msg(responseMsg)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);

        }
    }



    //문서 양식 관리 - 문서 양식 조회
    @GetMapping("/selDocType")
    public ResponseEntity<?> selDocType(Long fileNo){

        DocFormResp result = adminApprovalService.selDocType(fileNo);
        String formHtml = attachFileService.readHtmlFileToString(result.getAttachFileNo());

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("result", result);
        data.put("formHtml", formHtml);

        String responseMsg;
        ResponseDto responseDto;


        responseMsg = MessageEnu.valueOf(MessageEnu.SAVE_OK.name()).getTitle();
        responseDto = ResponseDto.builder()
                .code("200")
                .msg(responseMsg)
                .data(data)
                .build();

        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    //문서 양식 관리  - 문서 양식 삭제
    @DeleteMapping("/deleteDocType/{docFormNo}")
    public String deleteType(@PathVariable Long docFormNo, Model model){

        Boolean result = adminApprovalService.deleteDocType(docFormNo);
        if(result){
            log.info("결재문서양식 삭제 성공");
        }else {
            log.info("결재문서양식 삭제 실패");
        }

        List<DocFolderTreeResp> folderTreeRespList = adminApprovalService.getFolderDeptNo();

        model.addAttribute("folderTreeRespList", folderTreeRespList);

        return "admin/approval/more/folderTree";
    }

    //문서 양식 관리 - 문서 양식 폴더 추가
    @PostMapping("/insDocTypeFolder")
    public String insDocTypeFolder(@RequestBody DocTypeFolderReq req, Model model){
        log.info("insDocTypeFolder 실행됨");
        Boolean result = adminApprovalService.insDocTypeFolder(req);
        if(result){
            log.info("결재문서양식 폴더추가 성공");
        }else {
            log.info("결재문서양식 폴더추가 실패");
        }

        List<DocFolderTreeResp> folderTreeRespList = adminApprovalService.getFolderDeptNo();

        model.addAttribute("folderTreeRespList", folderTreeRespList);

        return "admin/approval/more/folderTree";
    }

    @PostMapping("/udtDocTypeFolder")
    public String udtDocTypeFolder(@RequestBody UdtFolderReq req, Model model){
        log.info("udtDocTypeFolder 실행됨");
        Boolean result = adminApprovalService.udtDocTypeFolder(req);
        if(result){
            log.info("결재문서양식 폴더명 변경 성공");
        }else {
            log.info("결재문서양식 폴더명 변경 실패");
        }

        List<DocFolderTreeResp> folderTreeRespList = adminApprovalService.getFolderDeptNo();

        model.addAttribute("folderTreeRespList", folderTreeRespList);

        return "admin/approval/more/folderTree";
    }

    //문서 양식 관리  - 문서 양식 삭제
    @DeleteMapping("/delDocTypeFolder/{docFolderNo}")
    public String delDocTypeFolder(@PathVariable Long docFolderNo, Model model){

        Boolean result = adminApprovalService.delDocTypeFolder(docFolderNo);
        if(result){
            log.info("결재문서양식 폴더 삭제 성공");
        }else {
            log.info("결재문서양식 폴더 삭제 실패");
        }

        List<DocFolderTreeResp> folderTreeRespList = adminApprovalService.getFolderDeptNo();

        model.addAttribute("folderTreeRespList", folderTreeRespList);

        return "admin/approval/more/folderTree";
    }

    @PostMapping("/selDocTypeFolder")
    public String selDocTypeFolder(Model model){

        log.info("dddddddddddddd");
        List<AprvFolderResp> aprvFolders = adminApprovalService.selAprvFolderList();
        model.addAttribute("aprvFolders", aprvFolders);
        return "admin/approval/more/changeSelectCard";
    }

    @PutMapping("/upFolderOrder")
    public  String upFolderOrder(@RequestBody Long folderNo, Model model){
        log.info("파일 순서 병경 실행됨");

        log.info("folderNo : "+ folderNo);

        Boolean result = adminApprovalService.upFolderOrder(folderNo);

        List<DocFolderTreeResp> folderTreeRespList = adminApprovalService.getFolderDeptNo();

        model.addAttribute("folderTreeRespList", folderTreeRespList);

        return "admin/approval/more/folderTree";
    }

    @PutMapping("/downFolderOrder")
    public  String downFolderOrder(@RequestBody Long folderNo, Model model){
        log.info("파일 순서 병경 실행됨");

        log.info("folderNo : "+ folderNo);

        Boolean result = adminApprovalService.downFolderOrder(folderNo);


        List<DocFolderTreeResp> folderTreeRespList = adminApprovalService.getFolderDeptNo();

        model.addAttribute("folderTreeRespList", folderTreeRespList);

        return "admin/approval/more/folderTree";
    }

    @GetMapping("/selDocFormList")
    public String selDocFormList(Model model) {

        List<DocFormListResp> list = adminApprovalService.selDocTypeList();

        model.addAttribute("docFormList", list);

        return "admin/approval/more/docForm-listboxCard";
    }

    @PutMapping("/udtDocFormOrder")
    public ResponseEntity<?> udtDocFormOrder(@RequestBody HashMap<String, Object> paramMap){

        Boolean result = adminApprovalService.udtDocFormOrder(paramMap);

        String responseMsg;
        ResponseDto responseDto;
        if (result) {
            responseMsg = MessageEnu.valueOf(MessageEnu.OK.name()).getTitle();
            responseDto = ResponseDto.builder()
                    .code("200")
                    .msg(responseMsg)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } else {
            responseMsg = MessageEnu.valueOf(MessageEnu.FAIL.name()).getTitle();
            responseDto = ResponseDto.builder()
                    .code("500")
                    .msg(responseMsg)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }
    }
}
