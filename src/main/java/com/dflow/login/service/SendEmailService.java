package com.dflow.login.service;

import com.dflow.login.responseDto.MailResp;

public interface SendEmailService {

    MailResp createMailAndChangePassword(String memberId, String memberEmail, String memberNameKr);
    String getTempPassword();
    void mailSend(MailResp req);
}
