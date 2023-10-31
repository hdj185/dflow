package com.dflow.serviceTest;

import com.dflow.board.responseDto.AttachFileInfo;
import com.dflow.board.service.BoardService;
import com.dflow.entity.ReferenceFile;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Log4j2
public class ReferenceFileServiceTest {
/*
    @Autowired
    private BoardService boardService;

    @Test
    @Transactional
    public void selFileList(){

        Long bno = 823L;

        List<AttachFileInfo> result = boardService.selAttachFileList(bno);
        for( int i = 0; i < result.size(); i++){
            log.info(result.get(i).getFileOriginName());
        }

    }*/
}
