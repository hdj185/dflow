package com.dflow.serviceTest;

import com.dflow.board.requestDto.BrdNoticePageReq;
import com.dflow.board.responseDto.BrdNoticeInfoResp;
import com.dflow.board.responseDto.BrdNoticePageResp;
import com.dflow.board.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTest {

   /* @Autowired
    private BoardService boardService;


    @Test
    public void searchALL(){

        BrdNoticePageReq req = new BrdNoticePageReq();
        req.setType("tcw");
        req.setKeyword("");
        req.setSize(10);
        req.setPage(1);

        BrdNoticePageResp<BrdNoticeInfoResp> resp = boardService.noticePageList(req);

        for(int i = 0; i < resp.getPageResponseList().size(); i++){

            log.info(resp.getPageResponseList().get(i).getWrtName());
            log.info(resp.getPageResponseList().get(i).getBoardNo());
        }

        log.info(resp.getTotal());

    }*/
}
