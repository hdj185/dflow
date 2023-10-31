package com.dflow.repositoryTest;

import com.dflow.entity.AnnualSetting;
import com.dflow.repository.AnnualSettingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AnnualSettingRepositoryTests {
    @Autowired AnnualSettingRepository annualSettingRepository;

//    @Test
//    void 기준정보생성() {
//        AnnualSetting setting = new AnnualSetting();
//        setting.setAnuSetResetDate("2023-01-01");
//        setting.setAnuSetIncrementYear(1L);
//        setting.setAnuSetIncrementCnt(2L);
//        setting.setAnuSetDefaultCnt(10L);
//        setting.setAnuSetMaxCnt(36L);
//        annualSettingRepository.save(setting);
//    }
}
