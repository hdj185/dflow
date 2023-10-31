package com.dflow.repositoryTest;

import com.dflow.entity.DocumentType;
import com.dflow.repository.DocumentTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ApprovalRepositoryTest {
    @Autowired
    private DocumentTypeRepository documentTypeRepository;

//    @Test
//    public void 양식목록테스트() {
//        List<DocumentType> list = documentTypeRepository.findByDocFormUseFlag("Y");
//        System.out.println("------------------------------------------------");
//        for(DocumentType type : list)
//            System.out.println(type.getDocFormTypeNo() + ", name: " + type.getDocFormName());
//        System.out.println("------------------------------------------------");
//    }

}
