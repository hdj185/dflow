package com.dflow.dashboard.controller;

import com.dflow.attachFile.service.AttachFileService;
import com.dflow.dashboard.requestDto.ReqMainRollbook;
import com.dflow.dashboard.responseDto.*;
import com.dflow.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/main")
@Log4j2
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/main")
    public String mainResponse(Model model, Principal principal) {

        //로그인한 사람 아이디
        String memberId = principal.getName();

        //출근 기준 정보
        RespMainStandard standardTime = dashboardService.selStandardTimeMain();
        model.addAttribute("standardTime", standardTime);

        //회원 정보
        RespMainMemberInfo memberInfo = dashboardService.selMemberInfoMain(memberId);
        model.addAttribute("memberInfo", memberInfo);

        //나의 출퇴근 정보
        RespMainRollbook rollbook = dashboardService.selRollbookMain(memberId);
        model.addAttribute("myRollbook", rollbook);

        //나의 연차 정보
        RespMainAnnual annualInfo = dashboardService.selAnnualMain(memberId);
        model.addAttribute("myAnnual", annualInfo);

        //결재 문서 정보
        RespMainAprv approval = dashboardService.selAprvMain(memberId);
        model.addAttribute("approval", approval);

        //공지사항 목록
        List<RespMainBoard> noticeList = dashboardService.selNoticeMain();
        model.addAttribute("noticeList", noticeList);

        //신규게시글 목록
        List<RespMainBoard> boardList = dashboardService.selBoardMain();
        model.addAttribute("boardList", boardList);

        //프로젝트 목록
        List<RespMainProject> projectList = dashboardService.selProjectMain(memberId);
        model.addAttribute("projectList", projectList);

        return "index/index";
    }

    //출퇴근 처리
    @PostMapping("/insRollbook")
    @ResponseBody
    public ResponseEntity<String> insRollbook(@RequestBody ReqMainRollbook request, Principal principal) {
        boolean result = false;

        if(request.getRollbookState().equals("정상 출근") || request.getRollbookState().equals("지각"))
            result = dashboardService.insRollbookMain(principal.getName(), request);
        else
            result = dashboardService.udtRollbookMain(principal.getName(), request);

        if(result)
            return ResponseEntity.ok(request.getRollbookState() + " 처리 완료되었습니다.");
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("요청에 실패하였습니다.");
    }
}
