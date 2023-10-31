package com.dflow.dashboard.responseDto;

import com.dflow.entity.Rollbook;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Data
@ToString
@Builder
@AllArgsConstructor
public class RespMainRollbook {
    String status;      //근무 상태 - 근무중, 퇴근, 미출근
    String openTime;    //출근 시간
    String closeTime;   //퇴근 시간

    //근태 정보가 아예 없을 때
    public RespMainRollbook() {
        this.status = "미출근";
        this.openTime = "-";
        this.closeTime = "-";
    }

    //entity -> dto 변환
    public static RespMainRollbook of(Rollbook rollbook) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String status;

        if(rollbook.getRollbookOpenState().equals("미출근")) // 미출근 찍혔다면 미출근
            status = "미출근";
        else if(rollbook.getRollbookCloseTime() == null)  // 퇴근 시간이 안 찍혀 있다면 근무중
            status = "근무중";
        else
            status = "퇴근";

        return RespMainRollbook.builder()
                .status(status)
                .openTime(rollbook.getRollbookOpenState().equals("미출근") ? "-" : rollbook.getRollbookOpenTime().format(formatter))
                .closeTime(status.equals("퇴근") ? rollbook.getRollbookCloseTime().format(formatter) : "-")
                .build();
    }
}
