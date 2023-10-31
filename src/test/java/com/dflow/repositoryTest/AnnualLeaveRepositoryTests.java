package com.dflow.repositoryTest;

import com.dflow.entity.AnnualLeave;
import com.dflow.entity.MemberInfo;
import com.dflow.repository.AnnualLeaveRepository;
import com.dflow.repository.MemberInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//import static com.dflow.entity.QMemberInfo.memberInfo;

@SpringBootTest
public class AnnualLeaveRepositoryTests {
    @Autowired
    AnnualLeaveRepository annualLeaveRepository;
    @Autowired
    MemberInfoRepository memberInfoRepository;


   /* @Test
    void 연차추가테스트 () {

        List<MemberInfo> list = memberInfoRepository.findAll();

        for (MemberInfo memberInfo1 : list) {
// 기본 연차 정보 생성
            AnnualLeave annualLeave = AnnualLeave.builder()
                    .annualCount(10D)
                    .annualLeft(10D)
                    .annualType("")
                    .annualEndDate(LocalDate.of(LocalDate.now().getYear(), 12, 31))
                    .memberNo(memberInfo1.getMemberNo())
                    .build();
            annualLeaveRepository.save(annualLeave);
        }
    }*/

//    @Test
//    void 특별연차추가테스트() {
//        Double annualCount = 15D;
//        AnnualLeave annual = AnnualLeave.builder()
//                .annualCount(annualCount)  //연차수
//                .annualLeft(annualCount)
//                .annualType("특별")
//                .annualEndDate(LocalDate.of(2023, 10, 1))
//                .memberNo(8L)
//                .build();
//        annualLeaveRepository.save(annual);
//    }

//    @Test
//    void 기본연차생성하기() {
//        List<AnnualLeave> list = new ArrayList<>();
//        List<MemberInfo> memberInfoList = memberInfoRepository.findAll();
//        for(MemberInfo member : memberInfoList) {
//            Long memberNo = member.getMemberNo();
//            if(memberNo == 8L || memberNo == 1L) continue;
//            AnnualLeave annualLeave = AnnualLeave.builder()
//                    .annualCount(0D)
//                    .annualLeft(0D)
//                    .annualType("")
//                    .annualEndDate(LocalDate.of(LocalDate.now().getYear(), 12, 31))
//                    .memberNo(memberNo)
//                    .build();
//            list.add(annualLeave);
//        }
//        annualLeaveRepository.saveAll(list);
//    }

//    @Test
//    void 퇴사자아닌연차정보불러오기() {
//        List<AnnualLeave> list = annualLeaveRepository.findAnnualLeavesByMemberFlag();
//        for(AnnualLeave leave : list) {
//            String msg = "no: " + leave.getAnnualNo() + ", memberName: " + leave.getMember().getMemberNameKr();
//            System.out.println(msg);
//        }
//    }
}
