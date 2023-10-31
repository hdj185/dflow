package com.dflow.repositoryTest;

import com.dflow.entity.CodeInfo;
import com.dflow.repository.CodeInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CodeInfoRepositoryTest {
//    @Autowired
//    CodeInfoRepository codeInfoRepository;

    /*
    @Test
    void listTest () {
        List<CodeInfo> codeInfoList = codeInfoRepository.findCodeInfoByCodeTypeAndCodeFlagFalse("PROJ_CODE");
        System.out.println("---------------------------------------test---------------------------------------");
        for(CodeInfo code : codeInfoList) {
            System.out.println(code.getCodeAccount());
        }
    }
     */
}
