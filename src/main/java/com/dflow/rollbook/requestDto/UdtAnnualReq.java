package com.dflow.rollbook.requestDto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UdtAnnualReq {
    Double totalAnnual;     //총 연차
    Double remainedAnnual;  //남은 연차
}
