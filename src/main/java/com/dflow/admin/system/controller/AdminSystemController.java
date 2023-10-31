package com.dflow.admin.system.controller;

import com.dflow.admin.organization.responseDto.AdminDepartStaffResp;
import com.dflow.admin.organization.responseDto.AdminMemberResp;
import com.dflow.admin.organization.responseDto.AdminUdtDepartment;
import com.dflow.admin.system.requestDto.LogInOutSearch;
import com.dflow.admin.system.responseDto.AuthoSelResp;
import com.dflow.admin.system.responseDto.LogCodeResp;
import com.dflow.admin.system.service.SystemService;
import com.dflow.organization.responseDto.DeptTreeResp;
import com.dflow.admin.system.responseDto.MemberLoginHistoryResp;
import com.dflow.admin.system.service.MemberLogService;
import com.dflow.project.responseDto.RespPage;
import com.dflow.project.responseDto.RespProjCode;
import com.dflow.rollbook.responseDto.AnnualLeaveRecordResp;
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

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin/system")
public class AdminSystemController {

    private final SystemService systemService;

    /**
     * 23-9-6 시스템 접근 권한 관리
     * **/
    private final MemberLogService memberLogService;

    //접근 권한 관리
    @GetMapping("/selAuthorization")
    public String selAuthorization(Model model) {

        List<AuthoSelResp> selAuthoList = systemService.selAuthoList();


        model.addAttribute("selAuthoList", selAuthoList);


        return "admin/system/admin_authorization";
    }




    // 임직원 수정할 값 받아오기
    @GetMapping("/udtMember/{memberNo}")
    public ResponseEntity<AuthoSelResp> getRoleChange(@PathVariable Long memberNo) {

        AuthoSelResp authoSelResp = systemService.getAdminMemberNo(memberNo);


        if (authoSelResp == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            // 조회 성공 시
            log.info(authoSelResp);

            return new ResponseEntity<>(authoSelResp, HttpStatus.OK);
        }
    }


    // 임직원 접근 권한 수정
    @PostMapping("/udtMember/{memberNo}")
    public ResponseEntity<?> postRoleChange(@PathVariable Long memberNo, @RequestBody AuthoSelResp authoSelResp) {

        AuthoSelResp udtAutho = systemService.udtMemberRole(memberNo, authoSelResp.getMemberRole());

        if (udtAutho == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            // 조회 성공 시
            log.info(udtAutho);

            return new ResponseEntity<>(udtAutho, HttpStatus.OK);
        }
    }


    //로그 관리
    @GetMapping("/selLog")
    public String selLog(Model model, LogInOutSearch search,
                         @RequestParam(defaultValue = "1", required = false) Integer page,
                         @RequestParam(name = "sortType", defaultValue = "desc") String sortType,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                         @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {

//        List<MemberLoginHistoryResp> logList = memberLogService.selAllMemberLogList();

        List<LogCodeResp> typeCodes = memberLogService.selLogCodeList("ACTIVITY_TYPE");
        model.addAttribute("typeCodes", typeCodes);  //구분 코드 리스트

        Sort sort = sortType.equals("asc") ? Sort.by("timestamp") : Sort.by("timestamp").descending();
        pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<MemberLoginHistoryResp> logHistoryPage = memberLogService.selLogList(search, pageable);

        RespPage pageInfo = new RespPage(logHistoryPage);

        model.addAttribute("logList", logHistoryPage.getContent());
        model.addAttribute("pageInfo", pageInfo); // 페이징 정보
        model.addAttribute("pageSize", pageSize); // 페이지수
        model.addAttribute("sortType", sortType); // 정렬 타입(최신순, 과거순)
        model.addAttribute("searchList", search);

        return "admin/system/admin_log";
    }
}
