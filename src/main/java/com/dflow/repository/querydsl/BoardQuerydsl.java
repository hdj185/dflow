package com.dflow.repository.querydsl;

import com.dflow.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardQuerydsl {

    //공지 게시판 리스트
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable, String boardNotice);
}
