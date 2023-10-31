package com.dflow.rollbook.requestDto;

import com.dflow.entity.Rollbook;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RollbookSearch {

    private String startDate;
    private String endDate;
    private String keyword;
    private String rollbookState;
    private String docType;
    private String memberId;
    private String memberName;
    private String rollbookDate;

    public LocalDateTime getStartDateTime() {
        return startDate == null || "".equals(startDate) ? null : LocalDate.parse(this.startDate).atStartOfDay();
    }

    public LocalDateTime getEndDateTime() {
        return endDate == null || "".equals(endDate) ? null : LocalDate.parse(this.endDate).atTime(23, 59, 59);
    }

    public LocalDate getStartDate() {
        return startDate == null || "".equals(startDate) ? null : LocalDate.parse(this.startDate);
    }

    public LocalDate getEndDate() {
        return endDate == null || "".equals(endDate) ? null : LocalDate.parse(this.endDate);
    }


}

