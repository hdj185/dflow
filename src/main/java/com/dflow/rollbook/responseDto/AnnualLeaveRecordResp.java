package com.dflow.rollbook.responseDto;

import com.dflow.entity.LeaveRecord;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnualLeaveRecordResp {
    private String leaveStartDate;  //휴가시작일
    private String leaveEndDate;    //휴가종료일
    private String leaveType;       //휴가종류
    private String leaveDay;        //휴가일수
    private String leaveUsed;       //차감일수
    private String createDate;      //신청일시
    private String aprvName;        //승인자
    private String aprvDate;        //승인일시
    private Double leaveUsedCnt;    //차감일수(휴가일수) double 버전


    //entity -> dto
    public static AnnualLeaveRecordResp of(LeaveRecord record) {
        return AnnualLeaveRecordResp.builder()
                .leaveStartDate(record.getLeaveStartDate().toString())
                .leaveEndDate(record.getLeaveEndDate().toString())
                .leaveType(record.getLeaveType())
                .leaveDay(doubleToString(Math.ceil(record.getLeaveUseCount())))
                .leaveUsed(doubleToString(record.getLeaveUseCount()))
                .createDate(datetimeToString(record.getDocument().getCreateDate()))
                .aprvName(record.getDocument().getUpdateMember().getMemberNameKr())
                .aprvDate(datetimeToString(record.getUpdateDate()))
                .leaveUsedCnt(record.getLeaveUseCount())
                .build();
    }

    //entity List -> dto List
    public static List<AnnualLeaveRecordResp> of(List<LeaveRecord> entityList) {
        List<AnnualLeaveRecordResp> list = new ArrayList<>();
        for(LeaveRecord record : entityList)
            list.add(of(record));
        return list;
    }

    //entity Page -> dto Page
    public static Page<AnnualLeaveRecordResp> of(Page<LeaveRecord> entityPage) {
        return new PageImpl<>(of(entityPage.getContent()), entityPage.getPageable(), entityPage.getTotalElements());
    }

    public static String doubleToString(double num) {
        return new DecimalFormat("#.##").format(num);
    }

    public static String datetimeToString(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    //dto Page -> total
    public static String getTotal(Page<AnnualLeaveRecordResp> page) {
        List<AnnualLeaveRecordResp> list = page.getContent();
        Double sum = 0d;
        for(AnnualLeaveRecordResp resp : list)
            sum += resp.getLeaveUsedCnt();
        return doubleToString(sum);
    }
}
