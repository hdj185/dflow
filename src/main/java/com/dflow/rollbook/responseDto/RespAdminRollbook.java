package com.dflow.rollbook.responseDto;


import com.dflow.entity.Rollbook;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RespAdminRollbook {

    private String openState;
    private String openTime;
    private String closeState;
    private String closeTime;
    private String content;   //비고




    public static RespAdminRollbook toDto(Rollbook rollbook){
        return RespAdminRollbook.builder()
                .openState(rollbook.getRollbookOpenState())
                .openTime(dateToString(rollbook.getRollbookOpenTime()))
                .closeState(rollbook.getRollbookCloseState() == null ? "미퇴근" : rollbook.getRollbookCloseState())
                .closeTime(rollbook.getRollbookCloseTime() == null ? null : dateToString(rollbook.getRollbookCloseTime()))
                .content(rollbook.getRollbookContents() == null ? "" : rollbook.getRollbookContents())
                .build();
    }



    private static String dateToString(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
