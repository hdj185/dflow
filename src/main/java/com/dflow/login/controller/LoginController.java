package com.dflow.login.controller;

import com.dflow.common.enumcode.MessageEnu;
import com.dflow.login.requestDto.*;

import com.dflow.login.responseDto.MailResp;
import com.dflow.login.responseDto.RespDeptLogin;
import com.dflow.login.responseDto.RespMdfMember;
import com.dflow.login.responseDto.RespStfLogin;
import com.dflow.login.service.LoginService;
import com.dflow.login.service.SendEmailService;
import com.dflow.repository.LoginOutHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;
    private final SendEmailService sendEmailService;
    private final LoginOutHistoryRepository loginOutHistoryRepository;


    @GetMapping(value = "/login")
    public String login(String error, String logout, Model model, HttpSession session) {

        List<RespDeptLogin> deptList = loginService.selDepartmentList();
        List<RespStfLogin> stfList = loginService.selStaffList();

        model.addAttribute("deptList", deptList);
        model.addAttribute("stfList", stfList);

        log.info("login get......");
        log.info("logout: " + logout);

        if(logout != null) {
            log.info("user logout......");
        }

        return "login/login";
    }

    @GetMapping(value = "/login/error")
    public String loginError(@RequestParam(value = "error") boolean error, @RequestParam(value = "exception") String exception, Model model) {

        log.info(error);
        log.info(exception);

        if (error) {
            model.addAttribute("loginErrorMsg", exception);

        } else {
            model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        }

        return "login/login";
    }
    

    @PostMapping("/join") // 회원 가입 처리를 위한 URL 매핑을 "/auth/join"로 설정
    public ResponseEntity<?> registerMember(@Valid @ModelAttribute AuthMemberRequest authMemberRequest,
                                            BindingResult bindingResult,
                                            @RequestParam("memberImg") MultipartFile memberImg) {
        if (bindingResult.hasErrors()) {
            String msg = "";
            for (FieldError error : bindingResult.getFieldErrors()) {
//                errorMessages.add(error.getDefaultMessage());
                msg += error.getDefaultMessage() + "\n";
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        boolean isNotValidate = loginService.validateDuplicateMember(authMemberRequest);

        if (isNotValidate) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageEnu.valueOf(MessageEnu.USER_EXIST.name()).getTitle());
        }

        boolean isRegistered = loginService.registerMember(authMemberRequest, memberImg);

        if (isRegistered) {
            return ResponseEntity.ok(MessageEnu.valueOf(MessageEnu.REGISTER_OK.name()).getTitle());
        } else {
            System.out.println("------------------오류오류-----------------------");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageEnu.valueOf(MessageEnu.REGISTER_FAIL.name()).getTitle());
        }
    }

    // 회원 정보 수정을 위해 회원 정보 가져오기
    @ResponseBody
    @GetMapping("/modify")
    public ResponseEntity<Map<String, Object>> modifyMember(Principal principal) {
        String memberId = principal.getName();
        Map<String, Object> response = new HashMap<>();

        RespMdfMember member = loginService.selModifyMemberInfo(memberId);
        response.put("member", member);

        List<RespDeptLogin> deptList = loginService.selDepartmentList();
        List<RespStfLogin> stfList = loginService.selStaffList();

        response.put("deptList", deptList);
        response.put("stfList", stfList);

        response.put("message", "요청이 성공적으로 처리되었습니다.");

        return ResponseEntity.ok(response);
    }

    // 회원 정보 수정 저장
    @PostMapping("/modify")
    public ResponseEntity<String> saveModifiedMember(@ModelAttribute AuthMemberRequest authMemberRequest,
                                                     @RequestParam("memberImg") MultipartFile memberImg,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = "";
            for (FieldError error : bindingResult.getFieldErrors()) {
//                errorMessages.add(error.getDefaultMessage());
                msg += error.getDefaultMessage() + "\n";
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        boolean isModified = loginService.saveModifiedMember(authMemberRequest, memberImg);

        if (isModified) {
            return ResponseEntity.ok("회원 정보 수정이 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 정보 수정을 실패하였습니다.");
        }
    }

    // 회원정보 수정 -> 비밀번호 확인
    @GetMapping("/checkPw")
    public ResponseEntity<String> checkPw(Principal principal, @RequestParam String checkPassword) {
        String memberId = principal.getName();

        boolean isMatches = loginService.checkPassword(memberId, checkPassword);

        if (isMatches) {
            return ResponseEntity.ok("비밀번호가 확인되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("현재 비밀번호와 일치하지 않습니다.");
        }
    }

    // 임시비번 발급 전 정보 확인
    @PostMapping("/confirmInfo")
    public ResponseEntity<Map<String, Boolean>> confirmInfo(@RequestBody ConfirmInfoReq confirmInfoReq) {

        Map<String, Boolean> json = new HashMap<>();
        boolean confirmed = loginService.memberEmailCheck(
                confirmInfoReq.getMemberId(),
                confirmInfoReq.getMemberEmail(),
                confirmInfoReq.getMemberNameKr());

        json.put("check", confirmed);

        // 클라이언트에게 응답을 반환합니다.
        return ResponseEntity.ok(json);
    }

    // 임시 비밀번호 날리기
    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestBody ConfirmInfoReq confirmInfoReq) {

        System.out.println("Received memberId: " + confirmInfoReq.getMemberId());
        System.out.println("Received memberEmail: " + confirmInfoReq.getMemberEmail());
        System.out.println("Received memberNameKr: " + confirmInfoReq.getMemberNameKr());


        try{

            MailResp req = sendEmailService.createMailAndChangePassword(
                    confirmInfoReq.getMemberId(),
                    confirmInfoReq.getMemberEmail(),
                    confirmInfoReq.getMemberNameKr());
            sendEmailService.mailSend(req);

        } catch (Exception e ) {

            e.printStackTrace();
        }

        // 클라이언트에게 응답을 반환합니다.
        return ResponseEntity.ok().build();
    }


    // 비밀번호 변경
    @PutMapping("/changePw")
    public ResponseEntity<String> changePw(Principal principal, @Valid @RequestBody ChangePasswordRequest changePasswordRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String msg = "";
            for (FieldError error : bindingResult.getFieldErrors()) {
//                errorMessages.add(error.getDefaultMessage());
                msg += error.getDefaultMessage() + "\n";
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        String memberId = principal.getName();

        // 회원 정보 수정
        boolean isChanged = loginService.changePassword(memberId, changePasswordRequest.getMemberPw());

        if (isChanged) {
            return ResponseEntity.ok("비밀번호가 변경되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경을 실패하였습니다.");
        }


//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(authMemberRequest.getMemberId(), authMemberRequest.getMemberPw())
//        );
//
//        /* 2. SecurityContextHolder 안에 있는 Context를 호출해 변경된 Authentication으로 설정 */
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//            return true;
//        }
    }

//    @PostMapping("/login")
//    public String login() {
//
//        try {
//            // 사용자 정보를 가져와 UserDetails 객체를 생성합니다.
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            // 로그인 처리를 위해 사용자 정보와 비밀번호를 담은 UsernamePasswordAuthenticationToken 객체를 생성합니다.
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                    userDetails, password, userDetails.getAuthorities());
//
//            // AuthenticationManager를 통해 로그인 처리를 합니다.
//            Authentication authentication = authenticationManager.authenticate(authToken);
//
//            // 인증이 성공하면 SecurityContext에 인증 객체를 설정합니다.
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // 로그인 성공 시 메인 페이지로 리다이렉트합니다.
//            return "redirect:/main/main";
//        } catch (Exception e) {
//            // 인증 실패 시 에러 메시지를 모델에 담아서 로그인 폼으로 다시 돌아갑니다.
//            model.addAttribute("error", "Invalid username or password");
//            return "/auth/login";
//        }
//    }


}
