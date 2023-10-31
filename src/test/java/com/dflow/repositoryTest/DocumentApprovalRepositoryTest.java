package com.dflow.repositoryTest;

import com.dflow.entity.DocumentApproval;
import com.dflow.repository.DocumentApprovalRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
@Log4j2
public class DocumentApprovalRepositoryTest {
//
//    @Autowired
//    private DocumentApprovalRepository documentApprovalRepository;
//
//    @Test
//    public void paging(){
//
//
//        Pageable pageable = PageRequest.of(0,10, Sort.by("docNo").descending());
//
//        Page<DocumentApproval> result = documentApprovalRepository.selTempDocumentApproval(pageable, 9L);
//
//        log.info(result.getTotalPages());
//
//        //pag size
//        log.info(result.getSize());
//
//        //pageNumber
//        log.info(result.getNumber());
//
//        //prev next
//        log.info(result.hasPrevious() + ": " + result.hasNext());
//
//        result.getContent().forEach(documentApproval -> log.info(documentApproval));
//    }
}
