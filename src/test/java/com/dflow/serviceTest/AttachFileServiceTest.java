package com.dflow.serviceTest;

import com.dflow.admin.approval.requestDto.AdminDocTypeInsReq;
import com.dflow.attachFile.service.AttachFileService;
import com.dflow.entity.AttachFile;
import com.dflow.repository.AttachFileRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
@Log4j2
public class AttachFileServiceTest {
    @Autowired
    AttachFileService attachFileService;
    @Autowired
    AttachFileRepository attachFileRepository;

//    @Test
//    void 파일불러오기테스트() {
//        Resource resource = attachFileService.selImgFile(1L);
//        System.out.println("-----------------------------test-----------------------------");
//        System.out.println(resource);
//        System.out.println("-----------------------------test-----------------------------");
//    }

//    @Test
//    void html파일만들기테스트() {
//        AdminDocTypeInsReq req = new AdminDocTypeInsReq();
//        req.setContents("content222222");
//        req.setDocFormName("helloWorld");
//        AttachFile attachFile = attachFileService.insHtmlFile(req);
//        System.out.println("-----------------------------test-----------------------------");
//        System.out.println(attachFile.getFileAttachNo());
//        System.out.println(attachFile.getFileOriginName());
//        System.out.println("-----------------------------test-----------------------------");
//    }

//    @Test
//    void html파일출력테스트() {
//        AttachFile attachFile = attachFileRepository.findById(103L).get();
//        String str = attachFileService.readHtmlFileToString(attachFile);
//        System.out.println("----------------------------------------test----------------------------------------");
//        System.out.println(str);
//        System.out.println("----------------------------------------test----------------------------------------");
//    }
}
