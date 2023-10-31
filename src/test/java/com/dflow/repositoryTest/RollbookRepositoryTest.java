package com.dflow.repositoryTest;

import com.dflow.dashboard.requestDto.ReqMainRollbook;
import com.dflow.entity.MemberInfo;
import com.dflow.entity.Rollbook;
import com.dflow.repository.MemberInfoRepository;
import com.dflow.repository.RollbookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RollbookRepositoryTest {
    @Autowired
    RollbookRepository rollbookRepository;
    @Autowired
    MemberInfoRepository memberInfoRepository;

//    @Test
//    void 멤버아이디_근태리스트_출력_테스트() {
//        String memberId = "a";
//        List<Rollbook> rollbooks = rollbookRepository.findAllByMemberId(memberId);
//        System.out.println("---------------------------------근태리스트 출력 테스트 시작---------------------------------");
//        for(Rollbook rollbook : rollbooks) {
//            System.out.println("직원: " + rollbook.getMember().getMemberNameKr());
//            System.out.println("출근상태: " + rollbook.getRollbookOpenState());
//            System.out.println("출근날짜: " + rollbook.getRollbookOpenTime());
//        }
//        System.out.println("---------------------------------근태리스트 출력 테스트 종료---------------------------------");
//    }

//    @Test
//    void 근태추가테스트() {
//        MemberInfo member = memberInfoRepository.findByMemberId("a123").get();
//        ReqMainRollbook req = new ReqMainRollbook();
//        req.setRollbookTime("31/08/2023, 08:55:53");
//        req.setRollbookState("출근");
//        Rollbook rollbook = req.toEntity(member);
//        rollbookRepository.save(rollbook);
//    }
}
