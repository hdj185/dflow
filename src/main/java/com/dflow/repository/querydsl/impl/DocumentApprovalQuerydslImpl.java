package com.dflow.repository.querydsl.impl;

import com.dflow.approval.requestDto.DocAprvSearch;
import com.dflow.dashboard.responseDto.RespMainAprv;
import com.dflow.entity.*;
import com.dflow.project.requestDto.ReqProjSearch;
import com.dflow.repository.querydsl.DocumentApprovalQuerydsl;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.time.LocalDateTime;

public class DocumentApprovalQuerydslImpl extends QuerydslRepositorySupport implements DocumentApprovalQuerydsl {
    public DocumentApprovalQuerydslImpl() {
        super(DocumentApproval.class);
    }

    // 메인용 결재 수
    @Override
    public Long selMainApprovalCounts(String memberId, String docState) {
        QDocumentApproval qDocumentApproval = QDocumentApproval.documentApproval;
        QApproval qApproval = QApproval.approval;
        JPQLQuery<DocumentApproval> query = from(qDocumentApproval);
        BooleanExpression conditions = qDocumentApproval.docFlag.eq("Y");

        // 현재 날짜에서 30일 전 날짜 계산
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        if (docState.equals("대기")) {
            BooleanExpression approverCondition = qApproval.aprvResult.eq("진행중")
                .and(qApproval.memberInfo.memberId.eq(memberId));
            conditions = conditions.and(qDocumentApproval.approvalList.any()
                .in(JPAExpressions.selectFrom(qApproval).where(approverCondition)));
        } else {
            conditions = conditions.and(
                qDocumentApproval.docState.eq(docState).and(qDocumentApproval.memberInfo.memberId.eq(memberId)));
        }

        // updateDate가 thirtyDaysAgo 이후인 문서만 고려
        conditions = conditions.and(qDocumentApproval.updateDate.after(thirtyDaysAgo));

        return query.where(conditions).fetchCount();
    }


    // 일반 전자결재  페이징
    @Override
    public Page<DocumentApproval> selDocumentApprovalsByConditions(Pageable pageable, DocAprvSearch search, Principal principal) {
        QDocumentApproval qDocumentApproval = QDocumentApproval.documentApproval;
        QApproval qApproval = QApproval.approval;
        BooleanExpression defaultConditions = qDocumentApproval.docFlag.eq("Y").and(qDocumentApproval.docState.ne("임시저장"));
        BooleanExpression conditions = defaultConditions;

        search.setMemberId(principal.getName());

        String memberId = principal.getName();


        // 기안자 , 참조가 현재 접속중인 유저의 id랑 같을 때  출력
        if(search.getMemberId() != null){
            //결재자 조건(아직 앞에서 승인 안 된 경우는 출력 안됨)
            BooleanExpression approverCondition = qApproval.aprvResult.ne("-").and(qApproval.memberInfo.memberId.eq(memberId));
            conditions =  conditions.and(qDocumentApproval.memberInfo.memberId.eq(memberId).or(
                qDocumentApproval.approvalList.any().in(JPAExpressions.selectFrom(qApproval).where(approverCondition))
            ));


            System.out.println(qDocumentApproval.approvalList.any().aprvResult.ne("-"));
        }



        // docState 별 Controller 매핑
        if(search.getDocState() != null) {
            if (search.getDocState().equals("반려/회수")) {
                conditions = conditions.and(qDocumentApproval.docState.eq("반려").or(qDocumentApproval.docState.eq("회수")));
            } else if (search.getDocState().equals("참조")) { // 서비스에서 한 세팅 참조
                // 리스트를 만들어서 레퍼런스 리스트의 멤버에서 멤버 아이디를 찾아 멤버아이디랑 비교
                conditions = defaultConditions.and(qDocumentApproval.referenceList.any().member.memberId.eq(memberId))
                    .and(qDocumentApproval.docState.ne("회수")); // 참조자 찾는거

            } else if(search.getDocType().equals("결재대기")) {
                conditions = defaultConditions.and(qDocumentApproval.referenceList.any().member.memberId.ne(memberId));
            }

            else {
                conditions = conditions.and(qDocumentApproval.docState.eq(search.getDocState())); // 같은 것만
            }

            if (search.getStartDateTime() != null) {
                conditions = conditions.and(qDocumentApproval.updateDate.goe(search.getStartDateTime()));
                System.out.println("스타트 데이트 타임 --------" + conditions);
            }



            if (search.getEndDateTime() != null) {
                conditions = conditions.and(qDocumentApproval.createDate.loe(search.getEndDateTime()));
                System.out.println("엔드 데이트 타임 --------" + conditions);
            }
            System.out.println(conditions + " -------------------------eleleler2klertgjkl;eryk;j " + search.getDocState());

        }

        if(search.getDateType() != null && !search.getDateType().isBlank()) {

            // 완료, 반려 일 때만 결재일이 보이니 완료 반려 문서만 조회 하는 조건 추가
            if (search.getDateType().equals("결재일")) {
                conditions = conditions.and(qDocumentApproval.docState.eq("완료").or(qDocumentApproval.docState.eq("반려")));

                //StartDate만 제공된 경우
                if (search.getStartDateTime() != null && search.getEndDateTime() == null) {
                    conditions = conditions.and(qDocumentApproval.updateDate.goe(search.getStartDateTime()));
                }

                //endDate만 제공된 경우
                if (search.getEndDateTime() != null && search.getStartDateTime() == null) {
                    conditions = conditions.and(qDocumentApproval.updateDate.loe(search.getEndDateTime()));
                }

            } else if (search.getDateType().equals("기안일")) {
                System.out.println("들어오는지 확인 111");

                //StartDate만 제공된 경우
                if (search.getStartDate() != null && search.getEndDateTime() == null) {
                    conditions = conditions.and(qDocumentApproval.createDate.goe(search.getStartDateTime()));
                    System.out.println(conditions + "StartDate만 제공 된 경우 ");
                }
                //endDate만 제공된 경우
                else if (search.getEndDateTime() != null && search.getStartDateTime()==null) {

                    conditions = conditions.and(qDocumentApproval.createDate.loe(search.getEndDateTime()));
                    System.out.println(conditions + "선택 조건  제공 된 경우 ");
                }

            }
        }


        // 검색
        if (search.getKeyword() != null &&
            !"".equals(search.getKeyword()) &&
            !" ".equals(search.getKeyword())) {
            System.out.println("여기가 검색 --------" + conditions);
            String keyword = "%" + search.getKeyword() + "%";

            BooleanBuilder searchConditions = new BooleanBuilder();
            searchConditions.or(qDocumentApproval.docTTL.likeIgnoreCase(keyword));
            searchConditions.or(qDocumentApproval.docState.likeIgnoreCase(keyword));
            searchConditions.or(qDocumentApproval.memberInfo.memberNameKr.likeIgnoreCase(keyword));
            searchConditions.or(qDocumentApproval.memberInfo.staff.staffName.likeIgnoreCase(keyword));
            searchConditions.or(qDocumentApproval.memberInfo.department.departmentName.likeIgnoreCase(keyword));
            searchConditions.or(qDocumentApproval.documentTypeInfo.docFormName.likeIgnoreCase(keyword));
            conditions = conditions.and(searchConditions);

        }




        OrderSpecifier<?> orderSpecifier;
        Sort.Order sortOrder = pageable.getSort().getOrderFor("docNo");
        if (sortOrder != null && sortOrder.isDescending()) {
            orderSpecifier = qDocumentApproval.docNo.desc();
        } else {
            orderSpecifier = qDocumentApproval.docNo.asc();
        }

        QueryResults<DocumentApproval> queryResults = from(qDocumentApproval)
            .where(conditions)
            .orderBy(orderSpecifier)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }


    // 관리자  페이징
    @Override
    public Page<DocumentApproval> selAllDocumentApprovals(Pageable pageable, DocAprvSearch search, Principal principal) {
        QDocumentApproval qDocumentApproval = QDocumentApproval.documentApproval;
        QApproval qApproval = QApproval.approval;
        BooleanExpression defaultConditions = qDocumentApproval.docFlag.eq("Y").and(qDocumentApproval.docState.ne("임시저장"));
        BooleanExpression conditions = defaultConditions;

        search.setMemberId(principal.getName());

        String memberId = principal.getName();

        if(search.getDateType() != null && !search.getDateType().isBlank()) {

            if (search.getDateType().equals("결재일")) {
                System.out.println(search.getDateType() + " ------------------------");
                conditions = conditions.and(qDocumentApproval.docState.eq("완료").or(qDocumentApproval.docState.eq("반려")));

                if (search.getStartDateTime() != null) {
                    conditions = conditions.and(qDocumentApproval.updateDate.goe(search.getStartDateTime()));
                }

                if (search.getEndDateTime() != null) {
                    conditions = conditions.and(qDocumentApproval.updateDate.loe(search.getEndDateTime()));
                }

            } else if (search.getDateType().equals("기안일")) {

                if (search.getStartDateTime() != null) {
                    LocalDateTime startOfDay = search.getStartDate().atStartOfDay();
                    conditions = conditions.and(qDocumentApproval.createDate.goe(startOfDay));
                }

                if (search.getEndDateTime() != null) {
                    LocalDateTime endOfDay = search.getEndDate().atTime(23, 59, 59);
                    conditions = conditions.and(qDocumentApproval.createDate.loe(endOfDay));
                }

            }
        }

        // 검색
        if (search.getKeyword() != null &&
            !"".equals(search.getKeyword()) &&
            !" ".equals(search.getKeyword())) {
            System.out.println("여기가 검색 --------" + conditions);
            String keyword = "%" + search.getKeyword() + "%";

            BooleanBuilder searchConditions = new BooleanBuilder();
            searchConditions.or(qDocumentApproval.docTTL.likeIgnoreCase(keyword));
            searchConditions.or(qDocumentApproval.docState.likeIgnoreCase(keyword));
            searchConditions.or(qDocumentApproval.memberInfo.memberNameKr.likeIgnoreCase(keyword));
            searchConditions.or(qDocumentApproval.memberInfo.staff.staffName.likeIgnoreCase(keyword));
            searchConditions.or(qDocumentApproval.memberInfo.department.departmentName.likeIgnoreCase(keyword));
            conditions = conditions.and(searchConditions);

        }


        OrderSpecifier<?> orderSpecifier;
        Sort.Order sortOrder = pageable.getSort().getOrderFor("docNo");
        if (sortOrder != null && sortOrder.isDescending()) {
            orderSpecifier = qDocumentApproval.docNo.desc();
        } else {
            orderSpecifier = qDocumentApproval.docNo.asc();
        }

        QueryResults<DocumentApproval> queryResults = from(qDocumentApproval)
            .where(conditions)
            .orderBy(orderSpecifier)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }

    @Override
    public Page<DocumentApproval> selTempDocumentApproval(Pageable pageable, Long memberNo) {

        QDocumentApproval docAprv = QDocumentApproval.documentApproval;
        JPQLQuery<DocumentApproval> query = from(docAprv).where(docAprv.createNo.eq(memberNo).and(docAprv.docState.eq("임시저장")).and(docAprv.docFlag.eq("Y")));

        this.getQuerydsl().applyPagination(pageable, query);

        List<DocumentApproval> list = query.fetch();


        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }
}
