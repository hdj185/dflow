package com.dflow.login.requestDto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "변경할 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d\\S]{4,20}$", message = "비밀번호는 공백 제외, 영어 대소문자와 숫자를 포함한 4자 이상 20자 이하여야 합니다.")
    private String memberPw;

    @NotBlank(message = "비밀번호를 한 번 더 입력해주세요.")
    private String confirmPw;

}
