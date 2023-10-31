package com.dflow.admin.board;

import com.dflow.board.requestDto.BrdNoticePageReq;
import com.dflow.board.requestDto.BrdUdtPageReq;
import com.dflow.board.responseDto.AttachFileInfo;
import com.dflow.board.responseDto.BrdNoticeInfoResp;
import com.dflow.board.responseDto.BrdNoticePageResp;
import com.dflow.board.service.BoardService;
import com.dflow.common.dto.ResponseDto;
import com.dflow.common.enumcode.MessageEnu;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin/board")
@Log4j2
public class AdminBoardController {
    private final BoardService boardService;
    //게시판 목록 조회
    @GetMapping("/selNoticeBoard")
    public String selNoticeBoard(Model model, @RequestParam(defaultValue = "1") int page, BrdNoticePageReq brdNoticePageReq) {
        brdNoticePageReq.changePage(page);

        BrdNoticePageResp<BrdNoticeInfoResp> pageResp = boardService.noticePageList(brdNoticePageReq);
        model.addAttribute("pageResp", pageResp);

        List<BrdNoticeInfoResp> noticeList = boardService.selNoticeList();
        model.addAttribute("noticeList", noticeList);
        return "admin/board/admin_notice_list";
    }

    // 게시글 상세 조회
    @GetMapping({"/selNoticeBoardDetail/{boardNo}", "/selBoardDetail/{boardNo}"})
    public String selBoardDetail(Model model, @PathVariable("boardNo") Long boardNo) {

        BrdNoticeInfoResp boardDetail = boardService.selDetailBoard(boardNo);

        List<AttachFileInfo> fileList = boardService.selAttachFileList(boardNo);

        model.addAttribute("boardDetail", boardDetail);
        model.addAttribute("fileList", fileList);
        return "admin/board/admin_board_detail";
    }

    //게시글 수정 페이지
    @GetMapping({"/udtNoticeBoard/{boardNo}", "/udtBoard/{boardNo}"})
    public String udtBoard(Model model, @PathVariable("boardNo") Long boardNo) {

        BrdNoticeInfoResp board = boardService.selDetailBoard(boardNo);
        List<AttachFileInfo> fileList = boardService.selAttachFileList(boardNo);
        HashMap<String, Object> urlList = new HashMap<String, Object>();

        log.info("공지 유무 : "


                +board.getBoardNotice());
        urlList.put("udtUrl", "/admin/board/udtBoard");

        model.addAttribute("urlList", urlList);
        model.addAttribute("board", board);
        model.addAttribute("fileList", fileList);
        return "admin/board/admin_board_edit";
    }

    // 게시글 수정
    @PostMapping(value = "/udtBoard")
    public ResponseEntity<?> udtBoardPage(@ModelAttribute BrdUdtPageReq udtPage,
                                          @RequestParam(value = "files", required = false) MultipartFile[] uploadFile,
                                          @RequestParam(value = "delFiles", required = false) List<Long> delFiles,
                                          Principal Principal) throws IOException {
        log.info("udtpage 들어옴");

        String memberName = Principal.getName();
        String boardType;
        log.info("delFiles : " + delFiles);
        log.info("udtpage : "+udtPage);

        if (udtPage.getNotice().equals("Y")) {
            boardType = "Y";
        } else {
            boardType = "N";
        }

        String responseMsg;
        ResponseDto responseDto;


        boolean result = boardService.udtBoard(udtPage, uploadFile, memberName, delFiles);
        if (result) {
            responseMsg = MessageEnu.valueOf(MessageEnu.SAVE_OK.name()).getTitle();
            responseDto = ResponseDto.builder()
                    .code("200")
                    .msg(responseMsg)
                    .data(boardType)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);

        } else {
            responseMsg = MessageEnu.valueOf(MessageEnu.NO_EXECUTE.name()).getTitle();
            responseDto = ResponseDto.builder()
                    .code("500")
                    .msg(responseMsg)
                    .data(boardType)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);

        }
    }
    

    ///////////////////////////컨트롤러 선 ////////////////////////////////////////

    @GetMapping("/selBoard")
    public String selBoard(Model model, @RequestParam(defaultValue = "1") int page, BrdNoticePageReq brdNoticePageReq) {
        brdNoticePageReq.changePage(page);

        BrdNoticePageResp<BrdNoticeInfoResp> pageResp = boardService.boardPageList(brdNoticePageReq);
        model.addAttribute("pageResp", pageResp);


        return "admin/board/admin_board_list";
    }

}
