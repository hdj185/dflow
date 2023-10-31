package com.dflow.rollbook.requestDto;

import com.dflow.entity.AnnualSetting;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAnuSetUdtReq {
    private String anuSetResetDate;    // 연차 생성 일자
    private Long anuSetIncrementYear;    // 가산 기준 연수   (년마다)
    private Long anuSetIncrementCnt;    // 가산개수   (년마다)
    private Long anuSetDefaultCnt;    // 기본 생성 수
    private Long anuSetMaxCnt;    // 최대 생성 수

    //dto -> 엔티티
    public AnnualSetting toEntity() {
        return AnnualSetting.builder()
                .anuSetNo(1L)
                .anuSetResetDate(this.getAnuSetResetDate())
                .anuSetIncrementYear(this.getAnuSetIncrementYear())
                .anuSetIncrementCnt(this.getAnuSetIncrementCnt())
                .anuSetDefaultCnt(this.getAnuSetDefaultCnt())
                .anuSetMaxCnt(this.getAnuSetMaxCnt())
                .build();
    }
}
