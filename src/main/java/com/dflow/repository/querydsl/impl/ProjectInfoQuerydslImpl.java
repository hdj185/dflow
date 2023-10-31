package com.dflow.repository.querydsl.impl;

import com.dflow.entity.*;
import com.dflow.project.requestDto.ReqProjSearch;
import com.dflow.repository.querydsl.ProjectInfoQuerydsl;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;

public class ProjectInfoQuerydslImpl extends QuerydslRepositorySupport implements ProjectInfoQuerydsl {
    public ProjectInfoQuerydslImpl() {
        super(MemberInfo.class);
    }

    @Override
    public Page<ProjectInfo> getProjectInfosByConditions(Pageable pageable, ReqProjSearch search) {
        QProjectInfo qProjectInfo = QProjectInfo.projectInfo;
        BooleanExpression conditions = qProjectInfo.projectIsDeleted.eq(false);

        if (search.getStartDateTime() != null) {
            conditions = conditions.and(qProjectInfo.projectEndDate.goe(search.getStartDate()));
        }

        if (search.getEndDateTime() != null) {
            conditions = conditions.and(qProjectInfo.projectStartDate.loe(search.getEndDate()));
        }

        if (search.getProjectType() != null && !"".equals(search.getProjectType())) {
            conditions = conditions.and(qProjectInfo.projectType.eq(search.getProjectType()));
        }

        if (search.getProjectState() != null && !"".equals(search.getProjectState())) {
            conditions = conditions.and(qProjectInfo.projectState.eq(search.getProjectState()));
        }

        //키워드 검색
        if (search.getKeywordType() != null &&
                !"".equals(search.getKeywordType()) &&
                search.getKeyword() != null &&
                !"".equals(search.getKeyword()) &&
                !" ".equals(search.getKeyword())) {
            switch (search.getKeywordType()) {
                case "투입인력":
                    conditions = conditions.and(qProjectInfo.resources.any().member.memberNameKr.eq(search.getKeyword()));
                    break;
                case "프로젝트명":
                    conditions = conditions.and(qProjectInfo.projectName.likeIgnoreCase("%" + search.getKeyword() + "%"));
                    break;
                case "설명":
                    conditions = conditions.and(qProjectInfo.projectDescription.likeIgnoreCase("%" + search.getKeyword() + "%"));
                    break;
            }
        }

        OrderSpecifier<?> orderSpecifier;
        Sort.Order sortOrder = pageable.getSort().getOrderFor("projectNo");
        if (sortOrder != null && sortOrder.isDescending()) {
            orderSpecifier = qProjectInfo.projectNo.desc();
        } else {
            orderSpecifier = qProjectInfo.projectNo.asc();
        }

        QueryResults<ProjectInfo> queryResults = from(qProjectInfo)
                .where(conditions)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }
}
