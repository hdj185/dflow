package com.dflow.admin.rollbook;

import com.dflow.admin.approval.responseDto.AprvCodeResp;
import com.dflow.admin.approval.responseDto.DocFormResp;
import com.dflow.approval.responseDto.DocAprvSelResp;
import com.dflow.common.dto.ResponseDto;
import com.dflow.common.enumcode.MessageEnu;
import com.dflow.dashboard.responseDto.RespMainStandard;
import com.dflow.dashboard.service.DashboardService;
import com.dflow.project.responseDto.RespPage;
import com.dflow.rollbook.requestDto.*;
import com.dflow.rollbook.responseDto.*;
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
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin/rollbook")
@Log4j2
public class AdminRollbookController {

    private final RollbookService rollbookService;
    private final DashboardService dashboardService;

    //연차 관리
    @GetMapping("/selAnnual")
    public String selAnnual(Model model) {
        AdminAnuSetResp annualSetting = rollbookService.selAdminAnuSetResp();
        model.addAttribute("annualSetting", annualSetting);
        return "admin/rollbook/admin_annual";
    }

    //연차 저장 요청
    @PutMapping("/udtAnnualSetting")
    public ResponseEntity<?> udtAnnualSetting(@RequestBody AdminAnuSetUdtReq req) {
        Boolean isUpdated = rollbookService.udtAdminAnnaulSetting(req);
        String msg = isUpdated ? MessageEnu.valueOf(MessageEnu.SAVE_OK.name()).getTitle() : MessageEnu.valueOf(MessageEnu.FAIL.name()).getTitle();
        ResponseDto responseDto = ResponseDto.builder()
                .code("200")
                .msg(msg)
                .build();
        return new ResponseEntity<>(responseDto, isUpdated ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 23-9-3
     * 관리자 근태
     **/
    @GetMapping("/selRollbook")
    public String selAdminRollbook(Model model, RollbookSearch search,
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "sortType", defaultValue = "desc") String sortType,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @PageableDefault(size = 20, sort = "rollbookDate", direction = Sort.Direction.DESC) Pageable pageable) {

        // 출근 기준 정보
        RespMainStandard standardTime = dashboardService.selStandardTimeMain();
        model.addAttribute("standardTime", standardTime);

        // 근태 코드 리스트
        List<AprvCodeResp> codeList = rollbookService.selRollbookCodeList();
        log.info("codeList:" + codeList);

        // 페이징 처리 만질 부분
        Sort sort = sortType.equals("asc") ? Sort.by("updateDate") : Sort.by("updateDate").descending();
        pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<RollbookPageResp> pageList = rollbookService.selAdminRollbookPagingList(pageable, search);

        log.info(pageList.getTotalElements());

        List<CorrectRollbookResp> correctList = rollbookService.selAdminCorrectionRollbookList();

        RespPage pageInfo = new RespPage(pageList);
        log.info(pageInfo);
        // model.addAttribute("rollGetContent", pageList.getContent()); // 리스트 정보
        // model.addAttribute("totalCount", rollPageList.getTotalElements()); // 총 데이터
        // 건수

        if (pageList == null || pageList.isEmpty()) {
            model.addAttribute("noRollbook", true);
        } else {
            model.addAttribute("rollbookList", pageList);
        }

        model.addAttribute("codeList", codeList); // 근태 코드 리스트(정상 출근/정상 퇴근/지각/조퇴/미출근/퇴근)
        model.addAttribute("correctList", correctList); // 정정 요청된 근태 리스트
        model.addAttribute("pageInfo", pageInfo); // 페이징 정보
        model.addAttribute("searchList", search);
        model.addAttribute("sortType", sortType); // 정렬 타입(최신순, 과거순)
        model.addAttribute("pageSize", pageSize); // 페이지수

        return "admin/rollbook/admin_rollbook";
    }

    /**
     * 관리자 근태
     **/
    @GetMapping("/selAdminEditRollbook/{rollbookNo}")
    public ResponseEntity<?> selAdminEditRollbook(@PathVariable("rollbookNo") Long rollbookNo) {

        RespAdminRollbook result = rollbookService.selAdminEditRollbook(rollbookNo);
        log.info("result=" + result.toString());

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("result", result);

        String responseMsg;
        ResponseDto responseDto;

        responseMsg = MessageEnu.valueOf(MessageEnu.OK.name()).getTitle();
        responseDto = ResponseDto.builder()
                .code("200")
                .msg(responseMsg)
                .data(data)
                .build();

        System.out.println("---------관리자 근태" + rollbookNo);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 23-9-4
     * 관리자 근태 삭제
     **/

    @PostMapping("/selRollbook")
    public ResponseEntity<?> returnDelete(@RequestParam("rollbookNo[]") List<Long> rollbookNo) {

        log.info("로그인포 확인");

        for(Long delNo : rollbookNo){
            rollbookService.delRollbook(delNo);
        }

        return  ResponseEntity.ok().body("성공");
    }

    // 근태 정보 수정
    @PutMapping("/udtAdminEditRollbook")
    public ResponseEntity<?> udtAdminEditRollbook(@RequestBody AdminRollbookUdtReq req) {

        Boolean isUpdated = rollbookService.udtAdminEditRollbook(req);
        ResponseDto responseDto;

        if (isUpdated) {
            responseDto = ResponseDto.builder()
                    .code("200")
                    .msg(MessageEnu.valueOf(MessageEnu.SAVE_OK.name()).getTitle())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } else {
            responseDto = ResponseDto.builder()
                    .code("200")
                    .msg(MessageEnu.valueOf(MessageEnu.FAIL.name()).getTitle())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 근태 기준 시간 수정
    @PutMapping("/udtAdminRollbookSetting")
    public ResponseEntity<?> udtAdminRollbookSetting(@RequestBody RollbookSettingUdtReq req) {
        Boolean isUpdated = rollbookService.udtRollbookSetting(req);
        ResponseDto responseDto;

        if (isUpdated) {
            responseDto = ResponseDto.builder()
                    .code("200")
                    .msg(MessageEnu.valueOf(MessageEnu.SAVE_OK.name()).getTitle())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } else {
            responseDto = ResponseDto.builder()
                    .code("200")
                    .msg(MessageEnu.valueOf(MessageEnu.FAIL.name()).getTitle())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
