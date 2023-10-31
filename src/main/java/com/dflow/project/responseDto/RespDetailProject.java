package com.dflow.project.responseDto;

import com.dflow.entity.ProjectInfo;
import com.dflow.entity.ProjectResource;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespDetailProject {
    private Long projectNo;
    private String projectName;     //프로젝트 이름
    private String projectType;     //프로젝트 구분
    private String projectDescription;        //프로젝트 설명
    private String projectStartDate;        //프로젝트 시작일
    private String projectEndDate;        //프로젝트 종료일
    private String projectState;        //프로젝트 상태
    private Integer projectProgress;        //프로젝트 진행률
    private Double projectManhour;  //프로젝트 투입공수(계획)
    private Integer projectResources;   //프로젝트 투입인력(계획)
    private String projectOverview;        //프로젝트 개요
    private String projectObjective;        //프로젝트 목적
    private String projectFeatures;        //프로젝트 기능
    private String projectRemarks;        //프로젝트 비고
    private String team;                //실제 투입입력: 이름(M/M)

    public static RespDetailProject of(ProjectInfo project) {
        return RespDetailProject.builder()
                .projectNo(project.getProjectNo())
                .projectName(project.getProjectName())
                .projectType(project.getProjectType())
                .projectDescription(project.getProjectDescription())
                .projectStartDate(dateToString(project.getProjectStartDate()))
                .projectEndDate(dateToString(project.getProjectEndDate()))
                .projectState(project.getProjectState())
                .projectProgress(project.getProjectProgress())
                .projectManhour(project.getProjectManhour())
                .projectResources(project.getProjectResources())
                .projectOverview(project.getProjectOverview())
                .projectObjective(project.getProjectObjective())
                .projectFeatures(project.getProjectFeatures())
                .projectRemarks(project.getProjectRemarks())
                .team(teamToString(project.getResources()))
                .build();
    }

    //Page<entity> -> Page<dto>
    public static Page<RespDetailProject> of(Page<ProjectInfo> entityList) {
        List<RespDetailProject> list = entityList.getContent().stream()
                .map(RespDetailProject::of)
                .collect(Collectors.toList());
        return new PageImpl<>(list, entityList.getPageable(), entityList.getTotalElements());
    }

    private static String dateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    //team(투입인력) 구하기
    public static String teamToString(List<ProjectResource> resourceList) {
        String result = "";
        for(int i = 0; i < resourceList.size(); i++) {
            ProjectResource resource = resourceList.get(i);

            //결과 넣기
            result += (i > 0 ? ", " : "")
                    + resource.getMember().getMemberNameKr() + "(" + resource.getResourceHours() + "M/M)";
        }
        return result;
    }
}
