package com.dflow.board.service;

import com.dflow.board.requestDto.BrdNoticePageReq;
import com.dflow.board.requestDto.BrdNoticeUploadPageReq;
import com.dflow.board.requestDto.BrdUdtPageReq;
import com.dflow.board.responseDto.AttachFileInfo;
import com.dflow.board.responseDto.BrdNoticeInfoResp;
import com.dflow.board.responseDto.BrdNoticePageResp;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BoardService {


    List<BrdNoticeInfoResp> selNoticeList();

    BrdNoticePageResp<BrdNoticeInfoResp> noticePageList(BrdNoticePageReq brdNoticePageReq);
    boolean insBoardNotice(BrdNoticeUploadPageReq pageReq, MultipartFile[] uploadFile, String memberName) throws IOException;
    boolean udtBoard(BrdUdtPageReq udtPageReq, MultipartFile[] uploadFile, String memberName, List<Long> delFiles) throws IOException;

    BrdNoticeInfoResp selDetailBoard(Long boardNo);

    BrdNoticePageResp<BrdNoticeInfoResp> boardPageList(BrdNoticePageReq brdNoticePageReq);

    List<AttachFileInfo> selAttachFileList(Long boardNo);
    Boolean delBoard(Long boardNo);
}
