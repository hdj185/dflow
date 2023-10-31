package com.dflow.board.requestDto;

import com.dflow.entity.Board;
import com.dflow.entity.MemberInfo;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrdUdtPageReq {

    /** 게시글 번호 **/
    private Long boardNo;
    /** 게시글 제목 **/
    private String title;
    /** 게시글 내용 **/
    private String content;
    /** 게시글 공지여부  **/
    private String notice;
    /** 게시글 공지 게시 시작기간 **/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate noticeDateStart;
    /** 게시글 공지 게시 종료기간 **/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate noticeDateEnd;
    /** 게시글 삭제 유무 **/
    private boolean boardFlag;

    public Board udtEntity(Board board, MemberInfo member){

        board.setUpdateNo(member.getUpdateNo());
        board.setBoardTitle(this.title);
        board.setBoardContent(this.content);
        board.setBoardNotice(this.notice);
        board.setBoardNotiDateStart(this.noticeDateStart);
        board.setBoardNotiDateEnd(this.noticeDateEnd);
        board.setBoardFlag(this.boardFlag);



        return board;
    }
}
