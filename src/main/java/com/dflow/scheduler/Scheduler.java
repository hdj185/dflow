package com.dflow.scheduler;

import com.dflow.rollbook.service.RollbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final RollbookService rollbookService;

    // 자정마다 퇴사자가 아닌 임직원의 근태 정보 insert하고, 전날 미퇴근자 체크하는 메소드
    @Scheduled(cron = "0 0 0 * * *")
    public void midnightRollbook() throws Exception {
        System.out.println("---------------------------------스케줄러 실행 시작---------------------------------");
        rollbookService.insMidnightRollbook();
        rollbookService.resetAnnualSetting();
        System.out.println("---------------------------------스케줄러 실행 완료---------------------------------");
    }
}
