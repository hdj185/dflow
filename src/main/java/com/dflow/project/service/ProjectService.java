package com.dflow.project.service;

import com.dflow.entity.ProjectInfo;
import com.dflow.project.requestDto.ReqEditProject;
import com.dflow.project.requestDto.ReqRegProject;
import com.dflow.project.requestDto.ReqResourceList;
import com.dflow.project.requestDto.ReqProjSearch;
import com.dflow.project.responseDto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    Page<ProjectInfo> selProjectInfoList(Pageable pageable, ReqProjSearch search);
    Page<RespDetailProject> selProjectList(Pageable pageable, ReqProjSearch search);
    RespDetailProject selDetailProject(Long projectNo);
    List<RespProject> selAllProjectList();
    ProjectInfo insProjectInfo(ReqRegProject project);
    ProjectInfo udtProjectInfo(ReqEditProject project);
    boolean delProjectInfo(Long projectNo);
    String insResourceList(ReqResourceList resourceList);
    String udtResourceList(ReqResourceList resourceList);
    Page<RespProjResource> selResourceProjList(Pageable pageable, ReqProjSearch search);
    List<RespProjDept> selResourceDepartmentList();
    Double selManhourMonth();
    List<RespProjCode> selProjCodeList(String codeType);
    RespEditResourceProj selEditResource(Long projectNo);
}
