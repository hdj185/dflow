package com.dflow.serviceTest;

import com.dflow.organization.requestDto.OrgSearchReq;
import com.dflow.organization.responseDto.DepartStaffMemberResp;
import com.dflow.organization.responseDto.DeptTreeResp;
import com.dflow.organization.service.OrganizationService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Log4j2
public class OrganizationServiceTest {

    @Autowired
    private OrganizationService organizationService;

//    @Test
//    public void getDeptTree(){
//        List<DeptTreeResp> result = organizationService.getDeptTreeResp();
//
//        log.info(result);
//    }
//
//    @Test
//    @Transactional
//    public void getMemList(){
//
//        List<DepartStaffMemberResp> result = organizationService.getMemberInfoListByDeptNo(5L);
//
//        for(int i = 0; i < result.size(); i++){
//            log.info(result.get(i).getMemberNameKr());
//        }
//
//    }
//
//    @Test
//    @Transactional
//    public void search(){
//
//        OrgSearchReq orgSearchReq = new OrgSearchReq();
//        orgSearchReq.setType("np");
//        orgSearchReq.setKeyword("김");
//
//        List<DepartStaffMemberResp> result = organizationService.searchMemberList(orgSearchReq);
//
//
//
//        for(int i = 0; i< result.size(); i++){
//            log.info(result.get(i).getMemberNameKr());
//        }
//
//    }
}
