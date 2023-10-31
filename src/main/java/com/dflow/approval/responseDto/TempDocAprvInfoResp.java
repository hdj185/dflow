package com.dflow.approval.responseDto;

import com.dflow.entity.DocumentApproval;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempDocAprvInfoResp {

    /* 전자결재문서 번호 */
    private Long docAprvNo;
    /* 문서양식명 */
    private String docFormName;
    /* 최종작성일 */
    private LocalDateTime updateDate;
    /* 작성자 */
    private String createName;


    @Service
    public static class TempDocAprvInfoMapper implements Function<DocumentApproval, TempDocAprvInfoResp> {

        @Override
        public TempDocAprvInfoResp apply(DocumentApproval documentApproval) {
            return TempDocAprvInfoResp.builder()
                    .docAprvNo(documentApproval.getDocNo())
                    .docFormName(documentApproval.getDocumentTypeInfo().getDocFormName())
                    .updateDate(documentApproval.getUpdateDate())
                    .createName(documentApproval.getMemberInfo().getMemberNameKr())
                    .build();
        }
    }

}
