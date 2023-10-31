package com.dflow.rollbook.requestDto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AnnualSearch {

    private String startDate;
    private String endDate;
    private String leaveType;   // 휴가종류: 연차, 반차, 반반차
    private String memberId;

    public LocalDate getStartDate() {
        return startDate == null || "".equals(startDate) ? null : LocalDate.parse(this.startDate);
    }

    public LocalDate getEndDate() {
        return startDate == null || "".equals(startDate) ? null : LocalDate.parse(this.endDate);
    }
}
