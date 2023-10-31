package com.dflow.dashboard.requestDto;

import com.dflow.entity.MemberInfo;
import com.dflow.entity.Rollbook;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqMainRollbook {
    private String rollbookState;   // 출퇴근 상태
    private String rollbookTime;    // 출퇴근 시간
    private String rollbookContents;    // 사유


    //Dto -> 엔티티 변환 (출근 등록용)
    public Rollbook toEntity(MemberInfo member) {
        return Rollbook.builder()
                .rollbookOpenState(this.rollbookState)
                .rollbookOpenTime(getRollbookTime())
                .rollbookContents(this.rollbookContents)
                .rollbookFlag("Y")
                .member(member)
                .build();
    }

    //Dto -> 엔티티 변환 (퇴근 업데이트용)
    public Rollbook toEntity(Rollbook rollbook) {
        rollbook.setRollbookCloseState(this.rollbookState);
        rollbook.setRollbookCloseTime(getRollbookTime());

        if(this.rollbookContents != null) {
            String content = this.rollbookContents;
            if(rollbook.getRollbookContents() != null)
                content = rollbook.getRollbookContents() + "\n" + content;
            rollbook.setRollbookContents(content);
        }

        return rollbook;
    }

    public LocalDateTime getRollbookTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss");
        return LocalDateTime.parse(this.rollbookTime, formatter);
    }
}
