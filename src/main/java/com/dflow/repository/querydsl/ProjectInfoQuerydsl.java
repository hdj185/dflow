package com.dflow.repository.querydsl;

import com.dflow.entity.ProjectInfo;
import com.dflow.project.requestDto.ReqProjSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectInfoQuerydsl {
    Page<ProjectInfo> getProjectInfosByConditions(Pageable pageable, ReqProjSearch search);
}
