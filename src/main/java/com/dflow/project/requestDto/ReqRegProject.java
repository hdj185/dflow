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
public class ReqRegProject {
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
    public ProjectInfo toEntity() {
        return ProjectInfo.builder()
                .projectName(this.projectName)
                .projectType(this.projectType)
                .projectDescription(this.projectDescription)
                .projectStartDate(LocalDate.parse(this.projectStartDate))
                .projectEndDate(LocalDate.parse(this.projectEndDate))
                .projectState(this.projectState)
                .projectProgress(this.projectProgress)
                .projectManhour(Double.parseDouble(this.projectManhour))
                .projectResources(Integer.parseInt(this.projectResources))
                .projectOverview(this.projectOverview)
                .projectObjective(this.projectObjective)
                .projectFeatures(this.projectFeatures)
                .projectRemarks(this.projectRemarks)
                .projectIsDeleted(false)
                .build();
    }
}
