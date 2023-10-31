package com.dflow.login.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailResp {
    private String address;
    private String title;
    private String message;
    private String memberId;
}
