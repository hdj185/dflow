package com.dflow.login;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class CustomUser extends User {
    private final Long memberNo;
    private final String nickname;
    public CustomUser(String username, String password, String authorities, Long memberNo, String nickname) {
        super(username, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + authorities)));
        this.memberNo = memberNo;
        this.nickname = nickname;
    }
}
