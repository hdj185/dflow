package com.dflow.serviceTest;

import com.dflow.admin.approval.responseDto.DocFolderTreeResp;
import com.dflow.admin.approval.responseDto.DocFormResp;
import com.dflow.admin.approval.service.AdminApprovalService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Log4j2
public class AdminApprovalServiceTest {
//    @Autowired
//    private AdminApprovalService adminApprovalService;
//
//
//    @Test
//    @Transactional
//    public void allList(){
//
//        List<DocFolderTreeResp> result = adminApprovalService.getFolderDeptNo();
//
//        log.info("result : "+result);
//
//    }
//
//    @Test
//    @Transactional
//    public void selDocType(){
//
//        Long dno = 11L;
//        DocFormResp result = adminApprovalService.selDocType(dno);
//
//        log.info("result : "+result);
//        log.info(result == null);
//    }
}
