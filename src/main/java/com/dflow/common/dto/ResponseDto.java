package com.dflow.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto {

    /** 응답 메시지 **/
    private String msg;

    /** 응답 코드 **/
    private String code;

    /** 응답 DATA **/
    private Object data;
}
