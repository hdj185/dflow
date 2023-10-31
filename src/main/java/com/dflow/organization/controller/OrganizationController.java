package com.dflow.organization.controller;

import com.dflow.common.dto.ResponseDto;
import com.dflow.common.enumcode.MessageEnu;
import com.dflow.organization.requestDto.OrgSearchReq;
import com.dflow.organization.responseDto.DepartStaffMemberResp;
import com.dflow.organization.responseDto.DeptTreeResp;
import com.dflow.organization.responseDto.OrgDetail;
import com.dflow.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/org")
public class OrganizationController {

    private final OrganizationService orgService;


    /**
     * 조직도 메인
     * @param model
     * @return
     * **/
    @GetMapping("selOrgMain")
    public String selDept(Model model) {

        // 부서 트리 조회
        List<DeptTreeResp> deptTreeRespList = orgService.getDeptTreeResp();
        log.info(deptTreeRespList);
        log.info("---------------------------------------");
        // 전체 직원 리시트 조회
        List<DepartStaffMemberResp> allList = orgService.getAllMemberList();

        HashMap<String, Object> urlList = new HashMap<String, Object>();


        urlList.put("detailUrl","/org/selOrgDetail");
        urlList.put("searchUrl","/org/searchMember");
        urlList.put("allUrl","/org/allMemberList");
        urlList.put("deptUrl","/org/deptMemberList");
        urlList.put("upDeptUrl","/org/upDeptMemberList");
        urlList.put("excelUrl","/excel");

        model.addAttribute("deptTreeRespList", deptTreeRespList);
        model.addAttribute("memberList", allList);
        model.addAttribute("urlList", urlList);


        return "organization/organization_deptmain";
    }
//
    /**
     * 조직도 직원 정보 상세 조회
     * @return
     * **/
    @GetMapping("/selOrgDetail")
    public ResponseEntity<?> selOrgDetail(Long memberNo){

        log.info(memberNo);
        OrgDetail orgDetail = orgService.selOrgDetail(memberNo);
        String responseMsg = MessageEnu.valueOf(MessageEnu.OK.name()).getTitle();
        if (orgDetail.equals("")) {
            responseMsg = MessageEnu.valueOf(MessageEnu.NO_DATA.name()).getTitle();
        }
        ResponseDto responseDto = ResponseDto.builder()
                .code("200")
                .msg(responseMsg)
                .data(orgDetail)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 조직도 부서별 직원 조회
     * @return
     * **/
    @GetMapping("/deptMemberList")
    public ResponseEntity<?> deptList(@RequestParam Optional<Long> departmentNo){

        Long deptNo =  departmentNo.orElse(0L);

        log.info(deptNo);

        List<DepartStaffMemberResp> memberList = orgService.getMemberInfoListByDeptNo(deptNo);
        String responseMsg = MessageEnu.valueOf(MessageEnu.OK.name()).getTitle();

        if (memberList.size() == 0) {
            responseMsg = MessageEnu.valueOf(MessageEnu.NO_DATA.name()).getTitle();
        }

        ResponseDto responseDto = ResponseDto.builder()
                .code("200")
                .msg(responseMsg)
                .data(memberList)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 조직도 전체 직원 조회
     * @return
     * **/
    @GetMapping("/allMemberList")
    public ResponseEntity<?> allList(){

        List<DepartStaffMemberResp> memberList = orgService.getAllMemberList();
        String responseMsg = MessageEnu.valueOf(MessageEnu.OK.name()).getTitle();

        if (memberList.size() == 0) {
            responseMsg = MessageEnu.valueOf(MessageEnu.NO_DATA.name()).getTitle();
        }

        ResponseDto responseDto = ResponseDto.builder()
                .code("200")
                .msg(responseMsg)
                .data(memberList)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 조직도 직원 검색
     * @return
     * **/
    @GetMapping("/searchMember")
    @ResponseBody
    public ResponseEntity<?> search(OrgSearchReq searchReq){

        List<DepartStaffMemberResp> memberList = orgService.searchMemberList(searchReq);
        String responseMsg = MessageEnu.valueOf(MessageEnu.OK.name()).getTitle();

        if (memberList.size() == 0) {
            responseMsg = MessageEnu.valueOf(MessageEnu.NO_DATA.name()).getTitle();
        }

        ResponseDto responseDto = ResponseDto.builder()
                .code("200")
                .msg(responseMsg)
                .data(memberList)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 상위 부서로 직원 목록 조회
     * @return
     * **/
    @GetMapping("/upDeptMemberList")
    public ResponseEntity<?> upDeptList(@RequestParam Optional<Long> departmentNo){

        Long deptNo =  departmentNo.orElse(0L);

        List<DepartStaffMemberResp> memberList = orgService.getMemberInfoListByUpDeptNo(deptNo);
        String responseMsg = MessageEnu.valueOf(MessageEnu.OK.name()).getTitle();

        if (memberList.size() == 0) {
            responseMsg = MessageEnu.valueOf(MessageEnu.NO_DATA.name()).getTitle();
        }

        ResponseDto responseDto = ResponseDto.builder()
                .code("200")
                .msg(responseMsg)
                .data(memberList)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
