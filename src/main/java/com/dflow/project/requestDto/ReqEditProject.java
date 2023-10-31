package com.dflow.project.requestDto;

import com.dflow.entity.MemberInfo;
import com.dflow.entity.ProjectInfo;
import lombok.*;

import java.time.LocalDate;

//프로젝트 등록 리퀘스트 Dto
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqEditProject {
    private Long projectNo;
    private String projectName;        //프로젝트 이름
    private String projectType;        //프로젝트 구분
    private String projectDescription;        //프로젝트 설명
    private String projectStartDate;        //프로젝트 시작일
    private String projectEndDate;        //프로젝트 종료일
    private String projectState;        //프로젝트 상태
    private int projectProgress;        //프로젝트 진행률
    private String projectManhour;        //프로젝트 계획 투입공수
    private String projectResources;        //프로젝트 계획 투입인력
    private String projectOverview;        //프로젝트 개요
    private String projectObjective;        //프로젝트 목적
    private String projectFeatures;        //프로젝트 기능
    private String projectRemarks;        //프로젝트 비고

    //dto -> entity
    public ProjectInfo toEntity(ProjectInfo entity) {
        entity.setProjectName(this.projectName);
        entity.setProjectType(this.projectType);
        entity.setProjectDescription(this.projectDescription);
        entity.setProjectStartDate(LocalDate.parse(this.projectStartDate));
        entity.setProjectEndDate(LocalDate.parse(this.projectEndDate));
        entity.setProjectState(this.projectState);
        entity.setProjectProgress(this.projectProgress);
        entity.setProjectManhour(Double.parseDouble(this.projectManhour));
        entity.setProjectResources(Integer.parseInt(this.projectResources));
        entity.setProjectOverview(this.projectOverview);
        entity.setProjectObjective(this.projectObjective);
        entity.setProjectFeatures(this.projectFeatures);
        entity.setProjectRemarks(this.projectRemarks);

        return entity;
    }
}
