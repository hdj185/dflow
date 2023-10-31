package com.dflow.dashboard.responseDto;

import com.dflow.entity.Board;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespMainBoard {
    private Long boardNo;
    private String member;      //작성자 이름
    private String title;       //제목
    private String createDate;  //작성 날짜
    private String createTime;  //작성 시간
    private boolean noticeFlag; //공지여부

    //공지사항 엔티티 -> Dto 변환
    public static RespMainBoard of(Board board) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter  = DateTimeFormatter.ofPattern("HH:mm");

        return RespMainBoard.builder()
                .boardNo(board.getBoardNo())
                .member(board.getCreateInfo().getMemberNameKr())
                .title(board.getBoardTitle())
                .createDate(board.getCreateDate().format(dateFormatter))
                .createTime(board.getCreateDate().format(timeFormatter))
                .noticeFlag(true)
                .build();
    }

    //일반 공지 엔티티 -> Dto 변환
    public static RespMainBoard ofGeneralNotice(Board board) {
        RespMainBoard response = of(board);
        response.setNoticeFlag(false);
        return response;
    }

    //엔티티 리스트 -> Dto 리스트 변환
    public static List<RespMainBoard> of(List<Board> boards) {
        List<RespMainBoard> list = new ArrayList<>();
        for(Board board : boards)
            list.add(of(board));
        return list;
    }

    //공지 엔티티 리스트 -> Dto 리스트 변환
    public static List<RespMainBoard> ofGeneralNotice(List<Board> boards) {
        List<RespMainBoard> list = new ArrayList<>();
        for(Board board : boards)
            list.add(ofGeneralNotice(board));
        return list;
    }
}
