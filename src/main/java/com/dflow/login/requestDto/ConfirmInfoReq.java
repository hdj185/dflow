package com.dflow.login.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmInfoReq {

    private String memberId;
    private String memberEmail;
    private String memberNameKr;

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
