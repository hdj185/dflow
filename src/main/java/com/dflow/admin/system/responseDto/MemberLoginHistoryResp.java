package com.dflow.admin.system.responseDto;

import com.dflow.entity.LeaveRecord;
import com.dflow.entity.LoginOutHistory;
import com.dflow.rollbook.responseDto.AnnualLeaveRecordResp;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginHistoryResp {

    private Long logNo;

    private String memberId;

    private String activityType;

    private LocalDateTime timestamp;

    private String clientIp;

    private String userAgent;

    public static MemberLoginHistoryResp of(LoginOutHistory entity) {

        return MemberLoginHistoryResp.builder() // .엔티티필드명(this.DTO필드명)
                .logNo(entity.getLogNo())
                .memberId(entity.getMemberId())
                .activityType(entity.getActivityType())
                .timestamp(entity.getTimestamp())
                .clientIp(entity.getClientIp())
                .userAgent(entity.getUserAgent())
                .build();
    }

    public static List<MemberLoginHistoryResp> of(List<LoginOutHistory> entityList) {
        List<MemberLoginHistoryResp> list = new ArrayList<>();
        for(LoginOutHistory entity : entityList)
            list.add(of(entity));
        return list;
    }

    public static Page<MemberLoginHistoryResp> of(Page<LoginOutHistory> entityPage) {
        return new PageImpl<>(of(entityPage.getContent()), entityPage.getPageable(), entityPage.getTotalElements());
    }
}
