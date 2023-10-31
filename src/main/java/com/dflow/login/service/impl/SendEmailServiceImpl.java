package com.dflow.login.service.impl;

import com.dflow.login.responseDto.MailResp;
import com.dflow.login.service.LoginService;
import com.dflow.login.service.SendEmailService;
import com.dflow.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SendEmailServiceImpl implements SendEmailService {

    private final MemberInfoRepository memberInfoRepository;
    private final LoginService loginService;

    private final JavaMailSender mailSender;

    private static final String FROM_ADDRESS = "depssujota@gmail.com";


    public MailResp createMailAndChangePassword(String memberId, String memberEmail, String memberNameKr) {

        String str = getTempPassword();

        System.out.println("---------------------");
        System.out.println("str:    " + str);
        System.out.println("아이디:    " + memberId);


        MailResp req = new MailResp();
        req.setAddress(memberEmail);
        req.setTitle(memberNameKr + "님의 D.FLOW 임시비밀번호 안내 이메일 입니다.");
        req.setMessage("안녕하세요. D.FLOW 임시비밀번호 안내 관련 이메일 입니다." + "[" + memberNameKr + "]" + "님의 임시 비밀번호는 "
                + str + " 입니다.");

        loginService.changePassword(memberId, str);

        return req;
    }

    public String getTempPassword() {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
                'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    public void mailSend(MailResp req) {
        if (req.getAddress() != null && !req.getAddress().isEmpty()) {
            System.out.println("이메일 전송 완료!");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(req.getAddress().trim());
            message.setFrom(FROM_ADDRESS);
            message.setSubject(req.getTitle());
            message.setText(req.getMessage());

            mailSender.send(message);
        } else {
            System.out.println("유효하지 않은 이메일 주소입니다.");
        }
    }
}
