package com.dflow.common.enumcode;

import lombok.Getter;

@Getter
public enum MessageEnu {

    NO_DATA("조회된 결과가 없습니다."),
    ERROR("시스템에 에러가 발생했습니다. 시스템 관리자에게 문의하세요."),
    USER_EXIST("이미 사용중인 ID 입니다."),
    USER_NOT_EXIST("사용가능한 ID 입니다."),

    TEL_NO_EXIST("이미 가입된 휴대전화 번호입니다."),
    TEL_NO_NOT_EXIST("사용가능한 휴대전화 번호입니다."),

    REGISTER_OK("회원 가입이 완료되었습니다."),
    REGISTER_FAIL("회원 가입에 실패했습니다."),

    NO_AUTH("권한이 없습니다. 관리자에게 문의하세요."),
    NO_EXECUTE("처리할 수 없습니다."),
    DELETE("삭제되었습니다."),
    SAVE_OK("저장되었습니다."),
    FAIL("처리에 실패하였습니다. 시스템 관리자에게 문의하세요."),
    OK("처리되었습니다."),
    NOT_OK("처리할 수 없습니다."),
    PW_FIND_OK("확인 되었습니다. 비밀번호를 재설정합니다. 새로운 비밀번호를 입력해주세요."),
    PW_FIND_FAIL("유효한 인증번호가 아니거나, 입력하신 정보와 일치하는 회원정보가 존재하지 않습니다."),
    ID_NOT_FOUND("유효한 인증번호가 아니거나, 입력하신 정보와 일치하는 아이디가 존재하지 않습니다."),
    CANCEL("취소"),

    SEND_EMAIL_OK("이메일이 발송되었습니다."),
    SEND_EMAIL_FAIL("이메일 발송에 실패하였습니다."),

    SEND_KAKAO_PUSH_OK("알림톡이 발송되었습니다."),
    SEND_KAKAO_PUSH_FAIL("알림톡 발송에 실패하였습니다."),

    PW_CHANGE_OK("변경 되었습니다. 변경된 비밀번호로 로그인 해주세요."),
    PW_CHANGE_FAIL("변경에 실패하였습니다. 다시 한번 확인 해주세요."),
    SEND_AUTH_NUM_OK("인증번호가 발송되었습니다."),
    SIGN_UP_SEND_AUTH_FAIL("인증번호 발송에 실패하였습니다."),
    SEND_AUTH_NUM_FAIL("입력하신 정보와 일치하는 회원정보가 존재하지 않습니다. \n 인증번호 발송에 실패하였습니다.");



    private String title;
    MessageEnu(String title) {
        this.title = title;
    }
}
