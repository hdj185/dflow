package com.dflow.rollbook.responseDto;

import com.dflow.entity.AnnualSetting;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAnuSetResp {
    private String anuSetResetDate;    // 연차 생성 일자
    private Long anuSetIncrementYear;    // 가산 기준 연수   (년마다)
    private Long anuSetIncrementCnt;    // 가산개수   (년마다)
    private Long anuSetDefaultCnt;    // 기본 생성 수
    private Long anuSetMaxCnt;    // 최대 생성 수

    //엔티티 -> dto
    public static AdminAnuSetResp of(AnnualSetting setting) {
        return AdminAnuSetResp.builder()
                .anuSetResetDate(setting.getAnuSetResetDate())
                .anuSetIncrementYear(setting.getAnuSetIncrementYear())
                .anuSetIncrementCnt(setting.getAnuSetIncrementCnt())
                .anuSetDefaultCnt(setting.getAnuSetDefaultCnt())
                .anuSetMaxCnt(setting.getAnuSetMaxCnt())
                .build();
    }
}
