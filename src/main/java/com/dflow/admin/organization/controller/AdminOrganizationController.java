package com.dflow.admin.organization.controller;

import com.dflow.admin.organization.requestDto.AdminDepartmentCreateReq;
import com.dflow.admin.organization.requestDto.AdminMemberReq;
import com.dflow.admin.organization.responseDto.AdminDepartStaffResp;
import com.dflow.admin.organization.responseDto.AdminDepartmentResp;
import com.dflow.admin.organization.responseDto.AdminMemberResp;
import com.dflow.admin.organization.responseDto.AdminUdtDepartment;
import com.dflow.admin.organization.service.AdminOrganizationService;
import com.dflow.common.dto.ResponseDto;
import com.dflow.common.enumcode.MessageEnu;
import com.dflow.entity.Department;
import com.dflow.entity.MemberInfo;
import com.dflow.login.CustomUser;
import com.dflow.organization.responseDto.DepartStaffMemberResp;
import com.dflow.organization.responseDto.DeptTreeResp;
import com.dflow.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.awt.*;
import java.lang.reflect.Member;
import java.security.Principal;
import java.util.*;
import java.util.List;;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin/org")
@Log4j2
public class AdminOrganizationController {

    private final AdminOrganizationService adminOrgService;
    private final OrganizationService orgService;


    // 부서 조회
    @GetMapping("/selDepart")
    public String selOrgStaff(Model model, Principal principal) {

        // adminOrgService를 사용하여 모든 부서 정보를 가져옵니다.
        List<AdminDepartmentResp> allDepartment = adminOrgService.getAllDepartment();


        // 23 - 8 - 11
        List<AdminUdtDepartment> departParentList = adminOrgService.departmentParentNoList(); // 상위 부서 셀렉
        List<AdminUdtDepartment> departChildList = adminOrgService.departmentChildNoList(); // 하위 부서만 셀렉

        // 현재 접속 중인 유저의 정보를 가져온다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) { // 인증이 안된 경우 예외 처리
            throw new IllegalStateException("사용자 인증 정보를 찾을 수 없습니다.");
        }
        // 커스텀 한 유저의 내역을 불러와서 매핑
        CustomUser currentUser = (CustomUser) authentication.getPrincipal();
        String createName = currentUser.getUsername();


        // 모든 부서 정보를 Model에 추가하여 View로 전달합니다.
        model.addAttribute("allDepartment", allDepartment);

        // 부서 자식 요소들 넘기는 부분
        model.addAttribute("departChildList", departChildList);

        // 부모 넘기는 부분
        model.addAttribute("departParentList", departParentList);

        // 현재 접속 자의 유저 이름
        model.addAttribute("createName", createName);

        return "admin/organization/admin_orgMain";
    }

    // 수정할 값을 받아오는 23-8-9
    @GetMapping("/udtDepart/{departmentNo}")
    public ResponseEntity<AdminUdtDepartment> getUdtOrgStaff(@PathVariable Long departmentNo, Model model,
                                                             AdminDepartmentCreateReq adminDepartmentCreateReq, Principal principal) {
        // 여기서 departmentNo를 이용해 데이터베이스 등에서 부서 정보를 조회하는 작업 수행
        List<Department> allDepartment = adminOrgService.findAll();
        log.info(allDepartment + "여기는 수정할 값을 받아오는 udtDepartment 컨트롤러");

        try {
            AdminUdtDepartment department = adminOrgService.getUdtAdminDepartmentNo(departmentNo);
            System.out.println(adminOrgService.getUdtAdminDepartmentNo(departmentNo));
            return ResponseEntity.ok(department);

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();

        }

    }

    /** 23-8-16
     *  조회 23-8-21  수정
     * **/

    // 임직원 조회
    @GetMapping("/selMember")
    public String selOrgMember(Model model) {


        // 부서 트리 조회
        List<DeptTreeResp> deptTreeRespList = orgService.getDeptTreeResp();
        log.info(deptTreeRespList + " ----------------- 1");

        // 어드민 임직원 조회
        List<AdminDepartStaffResp> departRespList = adminOrgService.toAdminMemberList();
        log.info(departRespList + " ----------------- 2");

        // 임직원 부서 리스트 조회  부서 번호가 not null 일 때 전부 출력 // 최종 23-8-20
        List<AdminUdtDepartment> adt = adminOrgService.adminMemberDepartList();
        model.addAttribute("adminMemberList", adt);
        log.info(departRespList + " ----------------- 3");

        // 임직원 직책 조회 // 23-8-20 최종
        List<AdminMemberResp> adminStaffList = adminOrgService.adminMemberStaffList();
        model.addAttribute("adminStaffList", adminStaffList);

        model.addAttribute("departRespList", departRespList);

        model.addAttribute("deptTreeRespList", deptTreeRespList);
        model.addAttribute("memberList", adt);



        return "admin/organization/admin_member";
    }

    /** 임직원  부서별 직원 조회 카드
     *  23 - 8 - 18  //  23 - 8 - 22 최종 수정
     * **/
    @PostMapping( "/selMember")
    public String selOrgMemberCard(@RequestParam(required = false,  defaultValue = "0") Long departmentNo, Model model) {

        if (departmentNo != 0 && departmentNo != null) {
            model.addAttribute("departRespList", adminOrgService.getMemberInfoListByDeptNo(departmentNo));
            return "admin/organization/card/adminMemberCard";
        } else if (departmentNo == 0) {

            log.info("0일때 " + departmentNo);

            model.addAttribute("departRespList", adminOrgService.toAdminMemberList());
            return "admin/organization/card/adminSelCard";

        }

        return "admin/organization/card/adminMemberCard";

    }




    /** 23 - 8 - 18
     * 조직도 부서별 직원 조회
     * @return
     * **/
    @GetMapping("/selMemberDepart")
    public ResponseEntity<?> deptList(@RequestParam @PathVariable Optional<Long> departmentNo){

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



    // 임직원 수정할 값 받아오기
    @GetMapping("/udtMember/{memberNo}")
    public ResponseEntity<AdminMemberResp> getUdtOrgMember(@PathVariable Long memberNo, Model model) {

        AdminMemberResp adminMemberResp = adminOrgService.getAdminMemberNo(memberNo);

        List<DepartStaffMemberResp> allList = orgService.getAllMemberList();

        model.addAttribute("allList", allList);

        if (adminMemberResp == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            // 조회 성공 시
            log.info(adminMemberResp);

            log.info(memberNo);
            log.info("여기는?");
            return new ResponseEntity<>(adminMemberResp, HttpStatus.OK);
        }
    }

    // 임직원 수정  // 부서랑 직책만 수정 // 최종 23-8-20
    @PostMapping("/udtMember/{memberNo}")
    public ResponseEntity<?> postUdtOrgMember(@PathVariable Long memberNo, @RequestBody AdminMemberResp adminMemberResp) {
        log.info("들어오나?");



        AdminMemberResp adminMemberResp1 = adminOrgService.udtAdminMember(memberNo, adminMemberResp);
        log.info("수정", memberNo, adminMemberResp1);
        System.out.println(adminMemberResp1);

        try {
            if (adminMemberResp1.getDepartmentParentNo() == null) {
                adminMemberResp1.setDepartmentParentNo(null);
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();

            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.ok(adminMemberResp1);
    }

    // 임직원 삭제
    @PostMapping("/delMember/{memberNo}")
    public String delOrgMember(@PathVariable Long memberNo) {

        return null;

    }

    /*
     * @PostMapping(value = "/childSelect/{departmentNo}")
     * public String childSelect(@PathVariable(value = "departmentNo") Long
     * departmentNo, Model model) {
     * List<AdminUdtDepartment> list = adminOrgService.getChild(departmentNo);
     * for(AdminUdtDepartment a : list) {
     * System.out.println(a);
     * }
     * model.addAttribute("departChildList", list);
     * return "admin/organization/admin_orgMain";
     * }
     */

    /** 부서추가 23 - 8 - 13 **/
    @PostMapping("/selDepart")
    public ResponseEntity<?> adminCreateDepartment(@RequestBody @Valid AdminDepartmentCreateReq adminDepartmentCreateReq) {

        // 현재 접속 중인 유저의 정보를 가져온다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) { // 인증이 안된 경우 예외 처리
            throw new IllegalStateException("사용자 인증 정보를 찾을 수 없습니다.");
        }

        // 부서 추가 메서드 실행
        Object obj = adminOrgService.createAdminDepartment(adminDepartmentCreateReq);


        log.info("최종 저장 되는 부분 " + obj);

        return new ResponseEntity<>(adminDepartmentCreateReq, HttpStatus.CREATED);
    }

    /** 부서 삭제 23 - 8 - 15 **/
    @DeleteMapping("/delDepart/{departmentNo}")
    public ResponseEntity<Void> deleteDepart(@PathVariable("departmentNo") Long departmentNo) {

        log.info("진짜 삭제인가?    1");

        adminOrgService.deleteDepartment(departmentNo);

        log.info("진짜 삭제인가      2?");
        return ResponseEntity.noContent().build();
    }

    /**
     *  23-9-7
     *  우선순위
     *  23-9-15
     *  우선순위 수정 위로 움직이는 부분
     * **/
    @PutMapping("/upDepartmentOrder/{departmentNo}")
    public ResponseEntity<String> upQueueDepartment(@PathVariable Long departmentNo, Model model) {

        // 스트링 반환//
       String result = adminOrgService.upDepartmentValue(departmentNo);
       log.info(result + " 어떤 동작을 수행? ");
       Department department = adminOrgService.maxQueueValue();
       log.info("여기서부터 대기  " + department.getDepartmentName());

       // ajax로 json 타입으로  받아야 한다.  // 예외 처리는 ControllerExceptionHandler에 처리
       return ResponseEntity.ok().body("{\"result\":\"" + result + "\"}");
    }

    /**
     *  23-9-16
     *  우선순위 수정 아래로 움직이는 부분
     * **/
    @PutMapping("/downDepartmentOrder/{departmentNo}")
    public ResponseEntity<String> downQueueDepartment(@PathVariable Long departmentNo, Model model) {

        // 스트링 반환//
        String result = adminOrgService.downDepartmentValue(departmentNo);
        log.info(result + " 어떤 동작을 수행? ");

        // ajax로 json 타입으로  받아야 한다.  // 예외 처리는 ControllerExceptionHandler에 처리
        return ResponseEntity.ok().body("{\"result\":\"" + result + "\"}");
    }


    // 부서 수정 23-8-9
    @PostMapping("/udtDepartPost/{departmentNo}")
    public ResponseEntity<?> udtOrgStaff(@PathVariable Long departmentNo,
                                         @RequestBody AdminUdtDepartment udtDepartment) {

        AdminUdtDepartment adminUdtDepartment = adminOrgService.udtAdminDepartment(departmentNo, udtDepartment);
        log.info("수정", departmentNo, udtDepartment);
        System.out.println(adminUdtDepartment);

        try {
            if (adminUdtDepartment.getDepartmentParentNo() == null) {
                adminUdtDepartment.setDepartmentParentNo(null);
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();

            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.ok(adminUdtDepartment);
    }






}
