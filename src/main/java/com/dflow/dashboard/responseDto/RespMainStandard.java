package com.dflow.dashboard.responseDto;

import com.dflow.entity.RollbookSetting;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespMainStandard {
    LocalTime openTime;     //기준 출근 시간
    LocalTime closeTime;    //기준 퇴근 시간

    //엔티티 -> DTO
    public static RespMainStandard of(RollbookSetting setting) {
        LocalDate today = LocalDate.now();
        return RespMainStandard.builder()
                .openTime(setting.getSettingOpenTime())
                .closeTime(setting.getSettingCloseTime())
                .build();
    }
}
