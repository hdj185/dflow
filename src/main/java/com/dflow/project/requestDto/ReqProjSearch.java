package com.dflow.project.requestDto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqProjSearch {
    private String startDate;
    private String endDate;
    private String projectType;     //projectType : SI, SM, SF
    private String projectState;    //진행상태 : 진행중, 대기, 완료, 반려 등
    private String keywordType;     //키워드에 대한 search 타입
    private String keyword;

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
