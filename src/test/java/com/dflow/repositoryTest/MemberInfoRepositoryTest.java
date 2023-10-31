package com.dflow.repositoryTest;

import com.dflow.entity.MemberInfo;
import com.dflow.repository.MemberInfoRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class MemberInfoRepositoryTest {

//    @Autowired
//    private MemberInfoRepository memberInfoRepository;
//
//    @Test
//    @Transactional
//    public void test(){
//
//        Optional<MemberInfo> result = memberInfoRepository.findById(1L);
//        MemberInfo memberInfo = result.orElseThrow();
//
//        log.info(memberInfo.getDepartment().getDepartmentName());
//
//    }
//
//    @Test
//    @Transactional
//    public void findAll(){
//
//        List<MemberInfo> result =  memberInfoRepository.findAllByDepartmentNoAndMemberFlagOrderByStaffNo(5L, "0");
//
//        log.info(result.size());
//        for(int i = 0; i< result.size(); i++){
//            log.info(result.get(i).getMemberNameKr());
//        }
//    }
//
//    @Test
//    @Transactional
//    public void testPagingList(){
//
//        String[] types = {"n","p"};
//        String keyword = "권석근";
//
//        List<MemberInfo> result = memberInfoRepository.searchDeptToMember(types, keyword);
//
//    }
//
//    @Test
//    @Transactional
//    public void all(){
//        List<MemberInfo> result = memberInfoRepository.findAlLByAndMemberFlagOrderByStaffNoAsc("0");
//
//        for(int i= 0; i<result.size(); i++){
//
//            log.info(result.get(i).getStaffNo());
//        }
//    }
}
