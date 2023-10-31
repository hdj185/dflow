package com.dflow.repository.querydsl.impl;


import com.dflow.admin.system.requestDto.LogInOutSearch;
import com.dflow.entity.LeaveRecord;
import com.dflow.entity.LoginOutHistory;
import com.dflow.entity.QLoginOutHistory;
import com.dflow.repository.querydsl.SystemQuerydsl;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class SystemQuerydslImpl extends QuerydslRepositorySupport implements SystemQuerydsl {

    public SystemQuerydslImpl() {
        super(LoginOutHistory.class);
    }

    @Override
    public Page<LoginOutHistory> selLogByConditions(Pageable pageable, LogInOutSearch search) {

        QLoginOutHistory qLoginOutHistory = QLoginOutHistory.loginOutHistory;

        BooleanExpression conditions = qLoginOutHistory.isNotNull();

        if (search.getStartDate() != null) {
            conditions = qLoginOutHistory.timestamp.goe(search.getStartDate());
        }

        if (search.getEndDate() != null) {
            conditions = conditions.and(qLoginOutHistory.timestamp.loe(search.getEndDate()));
        }


        // 로그 타입 검색
        if (search.getActivityType() != null && !"".equals(search.getActivityType())) {
            conditions = conditions.and(qLoginOutHistory.activityType.eq(search.getActivityType()));
        }


        //키워드 검색
        if (search.getKeywordType() != null &&
                !"".equals(search.getKeywordType()) &&
                search.getKeyword() != null &&
                !"".equals(search.getKeyword()) &&
                !" ".equals(search.getKeyword())) {
            String keyword = search.getKeyword();
            switch (search.getKeywordType()) {
                case "아이디":
                    conditions = conditions.and(qLoginOutHistory.memberId.eq(keyword));
                    break;
                case "클라이언트 IP":
                    conditions = conditions.and(qLoginOutHistory.clientIp.eq(keyword));
                    break;
            }
        }

        OrderSpecifier<?> orderSpecifier;
        Sort.Order sortOrder = pageable.getSort().getOrderFor("timestamp");
        if (sortOrder != null && sortOrder.isDescending()) {
            orderSpecifier = qLoginOutHistory.timestamp.desc();
        } else {
            orderSpecifier = qLoginOutHistory.timestamp.asc();
        }

        QueryResults<LoginOutHistory> queryResults = from(qLoginOutHistory)
                .where(conditions)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }

}
