package com.dflow.admin.system.responseDto;

import com.dflow.entity.CodeInfo;
import com.dflow.project.responseDto.RespProjCode;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogCodeResp {

    private String codeName;
    private String codeAccount;

    //entity -> dto
    public static LogCodeResp of(CodeInfo codeInfo) {
        return LogCodeResp.builder()
                .codeName(codeInfo.getCodeName())
                .codeAccount(codeInfo.getCodeAccount())
                .build();
    }

    public static List<LogCodeResp> of(List<CodeInfo> entityList) {
        List<LogCodeResp> list = new ArrayList<>();
        for(CodeInfo code : entityList)
            list.add(of(code));
        return list;
    }
}
