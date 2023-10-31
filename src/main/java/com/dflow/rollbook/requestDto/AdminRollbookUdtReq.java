package com.dflow.rollbook.requestDto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AdminRollbookUdtReq {
    Long rollbookNo;
    String openTime;
    String openState;
    String closeTime;
    String closeState;
    String contents;

    public LocalDateTime getOpenTime(LocalDate date) {
        return strToDateTime(date, openTime);
    }

    public LocalDateTime getCloseTime(LocalDate date) {
        if(closeState.equals("미퇴근")) {
            if(date.isEqual(LocalDate.now()))   //그날 미퇴근이면 아직 퇴근 안함(근무중)
                return null;
            else
                return date.atStartOfDay().plusDays(1).minusSeconds(1); //그날 오후 11시 59분
        } else
            return closeTime.isBlank() ? null : strToDateTime(date, closeTime);
    }

    private LocalDateTime strToDateTime(LocalDate date, String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalDateTime.of(date, LocalTime.parse(str, formatter));
    }
}
