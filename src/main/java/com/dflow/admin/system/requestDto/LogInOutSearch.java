package com.dflow.admin.system.requestDto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LogInOutSearch {

    private String startDate;
    private String endDate;
    private String activityType;    // 로그인, 로그아웃
    private String keywordType;
    private String keyword;


    public LocalDateTime getStartDate() {
        if (this.startDate == null || this.startDate.isEmpty()) {
            return null;
        }
        LocalDate date = LocalDate.parse(this.startDate);
        return date.atStartOfDay();
    }

    public LocalDateTime getEndDate() {
        if (this.endDate == null || this.endDate.isEmpty()) {
            return null;
        }
        LocalDate date = LocalDate.parse(this.endDate);
        return date.atTime(23, 59, 59);
    }
}
