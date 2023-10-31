package com.dflow.dashboard.responseDto;

import com.dflow.entity.AnnualLeave;
import lombok.*;

import java.text.DecimalFormat;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespMainAnnual {
    String totalAnnual;           //총연차
    String usedAnnual;            //연차 사용일
    String remainedAnnual;        //연차 잔여일

    public static RespMainAnnual of(List<AnnualLeave> list) {
        int total = 0;      //총 연차
        int remained = 0;   //연차 잔여일

        for(AnnualLeave leave : list) {
            total += leave.getAnnualCount();
            remained += leave.getAnnualLeft();
        }

        return RespMainAnnual.builder()
                .totalAnnual(String.valueOf(total))
                .usedAnnual(String.valueOf(total - remained))
                .remainedAnnual(String.valueOf(remained))
                .build();
    }

    //연차 정보 저장 테이블에서 레코드가 하나일 때
    public static RespMainAnnual of(AnnualLeave annual) {
        return RespMainAnnual.builder()
                .totalAnnual(doubleToString(annual.getAnnualCount()))
                .usedAnnual(doubleToString(annual.getAnnualCount() - annual.getAnnualLeft()))
                .remainedAnnual(doubleToString(annual.getAnnualLeft()))
                .build();
    }

    public static String doubleToString(double num) {
        return new DecimalFormat("#.##").format(num);
    }
}
