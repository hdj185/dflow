package com.dflow.repository.querydsl.impl;

import com.dflow.entity.*;

import com.dflow.project.requestDto.ReqProjSearch;
import com.dflow.repository.querydsl.RollbookQuerydsl;
import com.dflow.rollbook.requestDto.AnnualSearch;
import com.dflow.rollbook.requestDto.RollbookSearch;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class RollbookQuerydslImpl extends QuerydslRepositorySupport implements RollbookQuerydsl {

    public RollbookQuerydslImpl() {
        super(Rollbook.class);
    }


    /**
     * 23-9-3 관리자 근태 페이징 완료
     **/
    public Page<Rollbook> selAdminRollbookPaging(Pageable pageable, RollbookSearch search) {

        QRollbook qRollbook = QRollbook.rollbook;
        BooleanExpression conditions = qRollbook.rollbookFlag.eq("Y");

        /** 날짜로 검색 **/

        if (search.getRollbookDate() != null && !search.getRollbookDate().isEmpty()) {

            String yearMonthStr = search.getRollbookDate().substring(0, 7); // 'yyyy-MM' 부분만 추출

            YearMonth yearMonth = YearMonth.parse(yearMonthStr);
            LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            BooleanExpression dateCondition = qRollbook.createDate.between(startOfMonth, endOfMonth);
            conditions = conditions.and(dateCondition);

        }

        OrderSpecifier<?> orderSpecifier;
        Sort.Order sortOrder = pageable.getSort().getOrderFor("updateDate");
        System.out.println("-----------------------------------------------관리자근태" + sortOrder);
        if (sortOrder != null && sortOrder.isDescending()) {
            orderSpecifier = qRollbook.updateDate.desc();
        } else {
            orderSpecifier = qRollbook.updateDate.asc();
        }

        QueryResults<Rollbook> queryResults = from(qRollbook)
                .where(conditions)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }


    /**
     * 23-9-3 근태 페이징 완료 검색 미완
     **/
    @Override
    public Page<Rollbook> selRollbookPaging(Pageable pageable, RollbookSearch search, Principal principal) {
        search.setMemberId(principal.getName());

        QRollbook qRollbook = QRollbook.rollbook;
        BooleanExpression defaultConditions = qRollbook.member.memberId.eq(principal.getName()).and(qRollbook.rollbookFlag.eq("Y"));
        BooleanExpression conditions = defaultConditions;


        /** 날짜로 검색 **/

        if (search.getRollbookDate() != null && !search.getRollbookDate().isEmpty()) {

            String yearMonthStr = search.getRollbookDate().substring(0, 7); // 'yyyy-MM' 부분만 추출

            YearMonth yearMonth = YearMonth.parse(yearMonthStr);
            LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            BooleanExpression dateCondition = qRollbook.createDate.between(startOfMonth, endOfMonth);
            conditions = conditions.and(dateCondition);

        }

        OrderSpecifier<?> orderSpecifier;
        Sort.Order sortOrder = pageable.getSort().getOrderFor("rollbookDate");
        if (sortOrder != null && sortOrder.isDescending()) {
            orderSpecifier = qRollbook.rollbookNo.desc();
        } else {
            orderSpecifier = qRollbook.rollbookNo.asc();
        }

        QueryResults<Rollbook> queryResults = from(qRollbook)
                .where(conditions)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }


    /**
     * 연차 검색
     * 23-9-4
     **/
    @Override
    public Page<LeaveRecord> selAnnualByConditions(Pageable pageable, AnnualSearch search, Principal principal) {

        search.setMemberId(principal.getName());

        QLeaveRecord qLeaveRecord = QLeaveRecord.leaveRecord;
        BooleanExpression conditions = qLeaveRecord.member.memberId.eq(principal.getName());

        if (search.getStartDate() != null) {
            conditions = conditions.and(qLeaveRecord.leaveEndDate.goe(search.getStartDate()));
        }

        if (search.getEndDate() != null) {
            conditions = conditions.and(qLeaveRecord.leaveStartDate.loe(search.getEndDate()));
        }

        if (search.getLeaveType() != null && !"".equals(search.getLeaveType())) {
            conditions = conditions.and(qLeaveRecord.leaveType.eq(search.getLeaveType()));
//            System.out.println(conditions +"---------------------");
//
//
//            switch (search.getLeaveType()) {
//                case "연차":
//                    conditions = conditions.and(qLeaveRecord.leaveType.eq(search.getLeaveType()));
//                    System.out.println(conditions +"---------------------");
//                    break;
//                case "반차":
//                    conditions = conditions.and(qLeaveRecord.leaveType.eq(search.getLeaveType()));
//                    break;
//                case "반반차":
//                    conditions = conditions.and(qLeaveRecord.leaveType.eq(search.getLeaveType()));
//                    break;
//            }
        }

        OrderSpecifier<?> orderSpecifier;
        Sort.Order sortOrder = pageable.getSort().getOrderFor("leaveNo");
        if (sortOrder != null && sortOrder.isDescending()) {
            orderSpecifier = qLeaveRecord.leaveNo.desc();
        } else {
            orderSpecifier = qLeaveRecord.leaveNo.asc();
        }

        QueryResults<LeaveRecord> queryResults = from(qLeaveRecord)
                .where(conditions)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }
}
