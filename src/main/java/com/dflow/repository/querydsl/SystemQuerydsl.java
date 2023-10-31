package com.dflow.repository.querydsl;

import com.dflow.admin.system.requestDto.LogInOutSearch;
import com.dflow.entity.LoginOutHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SystemQuerydsl {

    Page<LoginOutHistory> selLogByConditions(Pageable pageable, LogInOutSearch search);
}
