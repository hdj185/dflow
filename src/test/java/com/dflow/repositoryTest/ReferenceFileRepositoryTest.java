package com.dflow.repositoryTest;

import com.dflow.entity.ReferenceFile;
import com.dflow.repository.ReferenceFileRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Log4j2
public class ReferenceFileRepositoryTest {
/*
    @Autowired
    private ReferenceFileRepository referenceFileRepository;


    @Test
    @Transactional
    public void selFileList(){

        Long bno = 823L;

        List<ReferenceFile> result = referenceFileRepository.findAllByBoardNo(bno);
        for( int i = 0; i < result.size(); i++){
            log.info(result.get(i).getAttachFile().getFileOriginName());
        }
    }*/
}
