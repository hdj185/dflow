package com.dflow.login.requestDto;

import lombok.*;
import org.springframework.security.web.csrf.CsrfToken;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginMemberReq {
    String memberId;
    String password;
    CsrfToken csrfToken;
}
