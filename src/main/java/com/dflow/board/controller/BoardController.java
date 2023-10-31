package com.dflow.board.controller;

import com.dflow.board.requestDto.BrdNoticePageReq;
import com.dflow.board.requestDto.BrdNoticeUploadPageReq;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/board")
@Log4j2
public class BoardController {

    private final BoardService boardService;

    //게시판 목록 조회
    @GetMapping("/selNoticeBoard")
    public String selNoticeBoard(Model model, @RequestParam(defaultValue = "1") int page, BrdNoticePageReq brdNoticePageReq) {
        brdNoticePageReq.changePage(page);

        BrdNoticePageResp<BrdNoticeInfoResp> pageResp = boardService.noticePageList(brdNoticePageReq);
        model.addAttribute("pageResp", pageResp);

        log.info("페이지 총 수 : "+pageResp.getTotal());

        List<BrdNoticeInfoResp> noticeList = boardService.selNoticeList();
        model.addAttribute("noticeList", noticeList);
        return "board/board_notice_list";
    }

    // 게시글 상세 조회?
    @GetMapping({"/selNoticeBoardDetail/{boardNo}", "/selBoardDetail/{boardNo}"})
    public String selBoardDetail(Model model, @PathVariable("boardNo") Long boardNo, Principal principal) {

        log.info("상세 페이지 들오옴~");

        String memberId = principal.getName();
        BrdNoticeInfoResp boardDetail = boardService.selDetailBoard(boardNo);
        List<AttachFileInfo> fileList = boardService.selAttachFileList(boardNo);


        model.addAttribute("boardDetail", boardDetail);
        model.addAttribute("fileList", fileList);
        model.addAttribute("memberId", memberId);
        return "board/board_detail";
    }


    // 게시판 글 작성 페이지
    @GetMapping({"/insNoticeBoard", "/insBoard"})
    public String insBoard(Model model) {

        HashMap<String, Object> urlList = new HashMap<String, Object>();

        urlList.put("uploadUrl", "/board/uploadNoticeBoard");

        model.addAttribute("urlList", urlList);

        return "board/board_sub";
    }

    /**
     * 게시글 등록
     **/
    @PostMapping(value = "/uploadNoticeBoard")
    public ResponseEntity<?> uploadNotice(@ModelAttribute BrdNoticeUploadPageReq uploadPage,
                                          @RequestParam(value = "files", required = false) MultipartFile[] uploadFile, Principal Principal) throws IOException {

        String memberName = Principal.getName();
//        uploadPage.setContent("게시글 등록 테스트 시작!!");
        String boardType;
        if (uploadPage.getNotice().equals("Y")) {
            boardType = "Y";
        } else {
            boardType = "N";
        }

        log.info(uploadPage.getNoticeDateStart());
        log.info(uploadPage.getNoticeDateEnd());

        String responseMsg;
        ResponseDto responseDto;

        boolean result = boardService.insBoardNotice(uploadPage, uploadFile, memberName);
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


    //게시글 수정 페이지
    @GetMapping({"/udtNoticeBoard/{boardNo}", "/udtBoard/{boardNo}"})
    public String udtBoard(Model model, @PathVariable("boardNo") Long boardNo) {

        BrdNoticeInfoResp board = boardService.selDetailBoard(boardNo);
        List<AttachFileInfo> fileList = boardService.selAttachFileList(boardNo);
        HashMap<String, Object> urlList = new HashMap<String, Object>();

        urlList.put("udtUrl", "/board/udtBoard");

        model.addAttribute("urlList", urlList);
        model.addAttribute("board", board);
        model.addAttribute("fileList", fileList);
        return "board/board_edit";
    }

    @PostMapping(value = "/udtBoard")
    public ResponseEntity<?> udtBoardPage(@ModelAttribute BrdUdtPageReq udtPage,
                                          @RequestParam(value = "files", required = false) MultipartFile[] uploadFile,
                                          @RequestParam(value = "delFiles", required = false) List<Long> delFiles,
                                          Principal Principal) throws IOException {

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

    //개선 요청사항 목록 조회
    @GetMapping("/selBoard")
    public String selBoard(Model model, @RequestParam(defaultValue = "1") int page, BrdNoticePageReq brdNoticePageReq) {
        brdNoticePageReq.changePage(page);

        BrdNoticePageResp<BrdNoticeInfoResp> pageResp = boardService.boardPageList(brdNoticePageReq);
        model.addAttribute("pageResp", pageResp);
        return "board/board_main_list";
    }

    //게시글 삭제
    @DeleteMapping("/delBoard/{boardNo}")
    public ResponseEntity<String> delBoard(@PathVariable("boardNo") Long boardNo) {
        boolean result = boardService.delBoard(boardNo);
        if (result) {
            return ResponseEntity.ok("게시물을 삭제하였습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 삭제에 실패하였습니다.");
        }
    }
}
