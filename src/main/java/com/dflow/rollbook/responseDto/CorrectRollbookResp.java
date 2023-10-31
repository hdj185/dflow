package com.dflow.rollbook.responseDto;

import com.dflow.entity.AdminRequest;
import com.dflow.entity.Rollbook;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CorrectRollbookResp {
    private Long rollbookNo;

    private String memberId;

    private String memberNameKr;

    private String rollbookDate;        // 날짜

    private String rollbookStatus;      // 근무상태

    private String rollbookOpenTime;    // 출근시간

    private String rollbookCloseTime;   // 퇴근시간

    private String rollbookContent;     // 비고

    // 근태 엔티티 -> dto 변화
    public static CorrectRollbookResp toDto(AdminRequest adminRequest) {
        Rollbook rollbook = adminRequest.getRollbook();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (E)");
        DateTimeFormatter timeFormatter  = DateTimeFormatter.ofPattern("HH:mm");

        ChronoLocalDateTime<?> midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);

        return CorrectRollbookResp.builder()
                .rollbookNo(rollbook.getRollbookNo())
                .rollbookDate(rollbook.getRollbookOpenTime().format(dateFormatter))
                .rollbookStatus(
                        (rollbook.getRollbookOpenState() != null ? rollbook.getRollbookOpenState() : (rollbook.getRollbookCloseState() != null ? rollbook.getRollbookCloseState() : "")))
                .rollbookOpenTime(
                        (rollbook.getRollbookOpenTime() == null && rollbook.getRollbookOpenTime().isAfter(midnight) ? "-" : (rollbook.getRollbookOpenTime() != null ? rollbook.getRollbookOpenTime().format(timeFormatter) : "")))
                .rollbookCloseTime(
                        (rollbook.getRollbookCloseTime() == null ? "-" : rollbook.getRollbookCloseTime().format(timeFormatter)))
                .rollbookContent(adminRequest.getRequestContent())
                .memberId(rollbook.getMember().getMemberId())
                .memberNameKr(rollbook.getMember().getMemberNameKr())
                .build();
    }

    public static List<CorrectRollbookResp> toDto(List<AdminRequest> entityList) {
        List<CorrectRollbookResp> list = new ArrayList<>();
        for(AdminRequest adminRequest : entityList)
            list.add(toDto(adminRequest));
        return list;
    }


}
