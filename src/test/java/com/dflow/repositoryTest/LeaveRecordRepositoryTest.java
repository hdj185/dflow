package com.dflow.repositoryTest;

import com.dflow.entity.LeaveRecord;
import com.dflow.repository.LeaveRecordRepository;
import com.dflow.rollbook.responseDto.AnnualLeaveRecordResp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
public class LeaveRecordRepositoryTest {
    @Autowired
    LeaveRecordRepository leaveRecordRepository;

//    @Test
//    public void test() {
//        Page<LeaveRecord> page = leaveRecordRepository.findLeaveRecordsByConditions("a", PageRequest.of(0, 20));
//        List<AnnualLeaveRecordResp> list = AnnualLeaveRecordResp.of(page).getContent();
//        System.out.println("-------------------------------------------test-------------------------------------------");
//        for(AnnualLeaveRecordResp resp : list)
//            System.out.println(resp.toString());
//        System.out.println("-------------------------------------------test-------------------------------------------");
//    }
}
