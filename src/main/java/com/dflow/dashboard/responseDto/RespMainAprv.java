package com.dflow.dashboard.responseDto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespMainAprv {
    private Long aprvProgress;  //진행
    private Long aprvCompleted; //완료
    private Long aprvRejected;  //반려
    private Long aprvPending;   //대기
    private Long aprvReference; //참조
}
