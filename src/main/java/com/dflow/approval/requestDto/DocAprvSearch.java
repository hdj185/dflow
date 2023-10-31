package com.dflow.approval.requestDto;

import lombok.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DocAprvSearch {

    private String startDate;
    private String endDate;
    private String keyword;
    private String docState;
    private String docType;
    private  String memberId;
    private String dateType;

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
