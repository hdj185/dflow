package com.dflow.repositoryTest;

import com.dflow.entity.Board;
import com.dflow.repository.BoardRepository;
import com.dflow.repository.MemberInfoRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberInfoRepository memberInfoRepository;
/*
    @Test
    public void testBoardInsert(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Board board = new Board();
            board.setBoardTitle("title..."+i);
            board.setBoardContent("content..."+i);
            board.setCreateInfo(memberInfoRepository.findById(13L).orElseThrow());
           board.setBoardNotiDate(LocalDate.now());


            Board result = boardRepository.save(board);
            log.info("BNO : "+result.getBoardNo());
        });
    }

    @Test
    public void boardList(){



    }*/
}
