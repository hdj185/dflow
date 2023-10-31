package com.dflow.rollbook.controller;

import com.dflow.approval.requestDto.DocAprvSearch;
import com.dflow.approval.responseDto.DocAprvSelResp;
import com.dflow.common.dto.ResponseDto;
import com.dflow.project.responseDto.RespPage;
import com.dflow.project.responseDto.RespProjCode;
import com.dflow.rollbook.requestDto.AnnualSearch;
import com.dflow.rollbook.requestDto.CorrectRollbookReq;
import com.dflow.rollbook.requestDto.RollbookSearch;
import com.dflow.rollbook.responseDto.*;
import com.dflow.rollbook.requestDto.UdtAnnualReq;
import com.dflow.rollbook.responseDto.AnnualCountResp;
import com.dflow.rollbook.responseDto.AnnualLeaveRecordResp;
import com.dflow.rollbook.responseDto.RespRollbookList;
import com.dflow.rollbook.responseDto.RollbookPageResp;
import com.dflow.rollbook.service.RollbookService;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/rollbook")
@Log4j2
public class RollbookController {

    private final RollbookService rollbookService;

    // 근태 기록 불러오기
    @GetMapping("/selRollbook")
    public String selRollbook(Model model, Principal principal,  RollbookSearch search,
                              @RequestParam(defaultValue = "1", required = false) Integer page,
                              @RequestParam(name = "sortType", defaultValue = "desc") String sortType,
                              @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                              @PageableDefault(size = 20, sort = "rollbookDate", direction = Sort.Direction.DESC) Pageable pageable) {

        String memberId = principal.getName();

        // 페이징 처리 만질 부분
        Sort sort = sortType.equals("asc") ? Sort.by("rollbookDate") : Sort.by("rollbookDate").descending();
        pageable = PageRequest.of(page - 1, pageSize, sort);

        List<RespRollbookList> respRollbookList = rollbookService.selRollbookList(memberId);
        Page<RollbookPageResp> pageList = rollbookService.sellRollbookPagingList(pageable, search, principal);

        RespPage pageInfo = new RespPage(pageList);
        log.info(pageInfo);
//        model.addAttribute("rollGetContent", pageList.getContent()); // 리스트 정보
//        model.addAttribute("totalCount", rollPageList.getTotalElements()); // 총 데이터 건수

        if (pageList == null || pageList.isEmpty()) {
            model.addAttribute("noRollbook", true);
        } else {
            model.addAttribute("rollbookList", pageList);
        }

        model.addAttribute("pageInfo", pageInfo); // 페이징 정보
        model.addAttribute("searchList", search);
        model.addAttribute("sortType", sortType); // 정렬 타입(최신순, 과거순)
        model.addAttribute("pageSize", pageSize); // 페이지수

        return "rollbook/rollbook";
    }

    @GetMapping("/selAnnual")
    public String selAnnual(Model model, Principal principal, AnnualSearch search,
                            @RequestParam(defaultValue = "1", required = false) Integer page,
                            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                            @PageableDefault(size = 35) Pageable pageable) {

        List<RespAnnualCode> typeCodes = rollbookService.selAnnualCodeList("ANNUAL_TYPE");

        String memberId = principal.getName();
        //연차 개수
        AnnualCountResp annualCount = rollbookService.selAnnualCount(memberId);
        model.addAttribute("annualCount", annualCount);

        pageable = PageRequest.of(page - 1, pageSize);

        //연차 페이지
        Page<AnnualLeaveRecordResp> recordPage = rollbookService.selLeaveRecordList(principal, search, pageable);

        RespPage pageInfo = new RespPage(recordPage);

        String totalUsedAnnual = AnnualLeaveRecordResp.getTotal(recordPage);    //휴가일수 합계 구하기

        model.addAttribute("typeCodes", typeCodes);  //구분 코드 리스트
        model.addAttribute("recordList", recordPage.getContent());
        model.addAttribute("pageInfo", pageInfo); // 페이징 정보
        model.addAttribute("pageSize", pageSize); // 페이지수
        model.addAttribute("totalCount", recordPage.getTotalElements());    //총 데이터 건수
        model.addAttribute("searchList", search);
        model.addAttribute("totalUsedAnnual", totalUsedAnnual); //휴가일수 합계

        return "rollbook/annual";
    }

    //연차 정보 수정
    @PutMapping("/udtAnnual")
    public ResponseEntity<?> udtAnnual(@RequestBody UdtAnnualReq req, Principal principal) {
        Boolean isUpdated = rollbookService.udtAnnualCount(req, principal.getName());

        if (isUpdated != null && isUpdated) {
            return ResponseEntity.ok("연차 정보가 수정되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("연차 정보 수정에 실패하였습니다.");
        }
    }

    //근태 정정 요청
    @PostMapping("/insCorrentRollbookRequest")
    public ResponseEntity<?> insCorrentRollbookRequest(@RequestBody CorrectRollbookReq req) {
        Boolean isSaved = rollbookService.insCorrentRollbookRequest(req);
        if (isSaved != null && isSaved) {
            return ResponseEntity.ok("정정신청이 접수되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("정정신청에 실패하였습니다.");
        }
    }
}
