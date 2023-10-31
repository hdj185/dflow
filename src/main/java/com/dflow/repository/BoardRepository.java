package com.dflow.repository;

import com.dflow.entity.Board;
import com.dflow.repository.querydsl.BoardQuerydsl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQuerydsl {

    //메인 대시보드에서 공지사항 List 출력
    @Query("SELECT b FROM Board b " +
            "WHERE b.boardFlag = false " +
            "AND b.boardNotice = 'Y' " +
            "AND b.boardNotiDateStart <= CURRENT_DATE " +
            "AND b.boardNotiDateEnd >= CURRENT_DATE " +
            "ORDER BY b.boardNo DESC")
    List<Board> findBoardsWithNoticeInMainNotice(Pageable pageable);

    //메인 대시보드에서 공지사항 List 출력
    @Query("SELECT b FROM Board b " +
            "WHERE b.boardFlag = false " +
            "AND b.boardNotice = 'Y' " +
            "ORDER BY b.boardNo DESC")
    List<Board> findBoardsInMainNotice(Pageable pageable);

    //메인 대시보드에서 신규게시글 List 출력
    @Query("SELECT b FROM Board b " +
            "WHERE b.boardFlag = false " +
            "AND (b.boardNotice IS NULL OR b.boardNotice = 'N') " +
            "ORDER BY b.boardNo DESC")
    List<Board> findBoardsInMain(Pageable pageable);

    //메인 대시보드에서 공지사항 List 출력
    @Query("SELECT b FROM Board b " +
            "WHERE b.boardFlag = false " +
            "AND b.boardNotice = 'Y' " +
            "AND b.boardNotiDateStart <= CURRENT_DATE " +
            "AND b.boardNotiDateEnd >= CURRENT_DATE " +
            "ORDER BY b.boardNo DESC")
    List<Board> findBoardsWithNotice();
}
