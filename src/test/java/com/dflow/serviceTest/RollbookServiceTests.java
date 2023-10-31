package com.dflow.serviceTest;

import com.dflow.rollbook.service.RollbookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RollbookServiceTests {
    @Autowired RollbookService rollbookService;
//
//    @Test
//    void 자동연차생성테스트() {
//        rollbookService.resetAnnualSetting();
//    }
}
