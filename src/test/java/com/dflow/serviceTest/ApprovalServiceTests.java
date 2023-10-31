package com.dflow.serviceTest;

import com.dflow.approval.requestDto.AprvUdtReq;
import com.dflow.approval.requestDto.TempDocAprvPageReq;
import com.dflow.approval.responseDto.TempDocAprvInfoResp;
import com.dflow.approval.responseDto.TempDocAprvResp;
import com.dflow.approval.service.ApprovalService;
import com.dflow.entity.DocumentApproval;
import com.dflow.entity.MemberInfo;
import com.dflow.repository.DocumentApprovalRepository;
import com.dflow.repository.MemberInfoRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ApprovalServiceTests {
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private MemberInfoRepository memberInfoRepository;
    @Autowired
    private DocumentApprovalRepository documentApprovalRepository;

//    @Test
//    void 결재라인테스트() {
//        MemberInfo member = memberInfoRepository.findById(4L).get();
//        AprvUdtReq req = new AprvUdtReq(254L, "", "승인", member.getMemberId());
//        System.out.println(approvalService.udtAprv(req));
//    }

//    @Test
//    public void searchALL(){
//
//        TempDocAprvPageReq req = new TempDocAprvPageReq();
//
//        req.setSize(10);
//        req.setPage(1);
//
//        TempDocAprvResp<TempDocAprvInfoResp> resp = approvalService.tempPageList(req, "admin");
//
//        for(int i = 0; i < resp.getPageResponseList().size(); i++){
//
//            log.info(resp.getPageResponseList().get(i).getDocFormName());
//            log.info(resp.getPageResponseList().get(i).getCreateName());
//        }
//
//        log.info(resp.getTotal());
//
//    }

//    @Test
//    public void htmlParseTest() {
//        DocumentApproval document = documentApprovalRepository.findById(316L).get();
//        approvalService.insLeaveReacord(document);
//    }
}
