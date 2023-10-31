package com.dflow.board.responseDto;

import com.dflow.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BrdNoticeInfoResp {

    /** 게시물 번호 **/
    private Long boardNo;

    /** 게시글 작성자 **/
    private String wrtName;

    /** 게시글 제목 **/
    private String boardTitle;

    /** 게시글 내용 **/
    private String boardContent;

    /** 게시글 공지 여부 **/
    private String boardNotice;

    /** 게시글 게시 시작기간 **/
    private LocalDate noticeDateStart;

    /** 게시글 게시 종료기간 **/
    private LocalDate noticeDateEnd;

    /** 게시글 작성일 **/
    private LocalDateTime createDate;

    /** 게시글 수정일 **/
    private LocalDateTime updateDate;

    private String memberId;

    @Service
    public static class BrdNoticeInfoMapper implements Function<Board, BrdNoticeInfoResp>{

        @Override
        public BrdNoticeInfoResp apply(Board board) {
            return BrdNoticeInfoResp.builder()
                .boardNo(board.getBoardNo())
                .wrtName(board.getCreateInfo().getMemberNameKr())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .boardNotice("N")
                .noticeDateStart(board.getBoardNotiDateStart())
                .noticeDateEnd(board.getBoardNotiDateEnd())
                .createDate(board.getCreateDate())
                .updateDate(board.getUpdateDate())
                .memberId(board.getCreateInfo().getMemberId())
                .build();
        }
    }

    //공지만 나오는 리스트 용도 - 엔티티 -> dto 변환
    public static BrdNoticeInfoResp of(Board board) {
        return BrdNoticeInfoResp.builder()
            .boardNo(board.getBoardNo())
            .wrtName(board.getCreateInfo().getMemberNameKr())
            .boardTitle(board.getBoardTitle())
            .boardContent(board.getBoardContent())
            .boardNotice("Y")
            .noticeDateStart(board.getBoardNotiDateStart())
            .noticeDateEnd(board.getBoardNotiDateEnd())
            .createDate(board.getCreateDate())
            .updateDate(board.getUpdateDate())
            .memberId(board.getCreateInfo().getMemberId())
            .build();
    }

    //공지만 나오는 리스트 용도 - 엔티티 List -> dto List 변환
    public static List<BrdNoticeInfoResp> of(List<Board> boards) {
        List<BrdNoticeInfoResp> list = new ArrayList<>();
        for(Board board : boards)
            list.add(of(board));
        return list;
    }

}
