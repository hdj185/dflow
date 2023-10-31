package com.dflow.repository.querydsl.impl;

import com.dflow.entity.MemberInfo;
import com.dflow.entity.QMemberInfo;
import com.dflow.repository.querydsl.MemberInfoQuerydsl;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class MemberInfoQuerydslImpl extends QuerydslRepositorySupport implements MemberInfoQuerydsl{

    public MemberInfoQuerydslImpl(){super(MemberInfo.class);}


    @Override
    public List<MemberInfo> findAllByDepartmentNo(Long departmentNo) {
        return null;
    }

    @Override
    public List<MemberInfo> searchDeptToMember(String[] types, String keyword) {

        QMemberInfo memberInfo = QMemberInfo.memberInfo;
        JPQLQuery<MemberInfo> query = from(memberInfo);

        if((types != null && types.length >0) && keyword != null){

            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type : types){

                switch (type){
                    case "n":
                        booleanBuilder.or(memberInfo.memberNameKr.contains(keyword));
                        break;
                    case "p":
                        booleanBuilder.or(memberInfo.memberPhone.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }
        query.where(memberInfo.memberNo.gt(0L)
                .and(memberInfo.memberFlag.eq("0")));
        query.orderBy(memberInfo.staffNo.asc());
        List<MemberInfo> list = query.fetch();

        return list;
    }
}
