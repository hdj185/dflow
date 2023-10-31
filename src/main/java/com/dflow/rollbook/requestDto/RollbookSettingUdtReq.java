package com.dflow.rollbook.requestDto;

import lombok.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RollbookSettingUdtReq {
    private String openTime;    //출근 시간
    private String closeTime;   //퇴근 시간

    public LocalTime getOpenTime() {
        return strToTime(openTime);
    }

    public LocalTime getCloseTime() {
        return strToTime(closeTime);
    }

    private LocalTime strToTime(String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(timeString, formatter);
    }
}
