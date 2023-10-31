package com.dflow.rollbook.responseDto;

import com.dflow.entity.AnnualLeave;
import lombok.*;

import java.text.DecimalFormat;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnualCountResp {
    String totalAnnual;           //총연차
    String usedAnnual;            //연차 사용일
    String remainedAnnual;        //연차 잔여일
    String annualValidity;      //연차 유효기간 (가장 가까운 유효기간)

    public static AnnualCountResp of(List<AnnualLeave> list) {
        double total = 0D;      //총 연차
        double remained = 0D;   //연차 잔여일

        for(AnnualLeave leave : list) {
            total += leave.getAnnualCount();
            remained += leave.getAnnualLeft();
        }

        return AnnualCountResp.builder()
                .totalAnnual(doubleToString(total))
                .usedAnnual(doubleToString(total - remained))
                .remainedAnnual(doubleToString(remained))
                .annualValidity(list.isEmpty() ? "-" : list.get(0).getAnnualEndDate().toString())
                .build();
    }

    public static AnnualCountResp of(AnnualLeave annual) {
        return AnnualCountResp.builder()
                .totalAnnual(doubleToString(annual.getAnnualCount()))
                .usedAnnual(doubleToString(annual.getAnnualCount() - annual.getAnnualLeft()))
                .remainedAnnual(doubleToString(annual.getAnnualLeft()))
                .annualValidity(annual.getAnnualEndDate().toString())
                .build();
    }

    public static String doubleToString(double num) {
        return new DecimalFormat("#.##").format(num);
    }
}
