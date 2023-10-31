package com.dflow.repository.querydsl.impl;

import com.dflow.entity.Board;
import com.dflow.entity.QBoard;
import com.dflow.repository.querydsl.BoardQuerydsl;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
@Log4j2
public class BoardQuerydslImpl extends QuerydslRepositorySupport implements BoardQuerydsl {


    public BoardQuerydslImpl() {super(Board.class);}

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable, String boardNotice) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board).where(board.boardFlag.eq(false));

        //공지 여부
        query.where(boardNotice != null && boardNotice.equals("Y") ? board.boardNotice.eq("Y") : board.boardNotice.isNull().or(board.boardNotice.eq("N")));

        if((types != null && types.length > 0) && keyword != null){

            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type : types){

                switch (type){
                    case "t":
                        booleanBuilder.or(board.boardTitle.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.boardContent.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.createInfo.memberNameKr.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }
        query.where(board.boardNo.gt(0L));
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }
}
