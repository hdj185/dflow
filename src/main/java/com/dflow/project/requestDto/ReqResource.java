package com.dflow.project.requestDto;

import com.dflow.entity.MemberInfo;
import com.dflow.entity.ProjectInfo;
import com.dflow.entity.ProjectResource;
import lombok.*;

import java.time.LocalDate;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqResource {
    private String memberName;          //투입인력 직원 이름
    private String resourceStartDate;   //투입 시작일
    private String resourceEndDate;     //투입 종료일
    private String resourceDuration;    //투입 기간
    private String resourceProgress;       //참여율
    private String resourceHours;       //실질 투입공수

    //dto -> entity
    public ProjectResource toEntity(ProjectInfo project, MemberInfo member) {
        return ProjectResource.builder()
                .project(project)
                .member(member)
                .resourceStartDate(LocalDate.parse(resourceStartDate))
                .resourceEndDate(LocalDate.parse(resourceEndDate))
                .resourceDuration(Long.parseLong(resourceDuration))
                .resourceProgress(Integer.parseInt(resourceProgress))
                .resourceHours(Double.parseDouble(resourceHours))
                .build();
    }

    //업데이트용
    public ProjectResource toEntity(ProjectResource resource, ProjectInfo project, MemberInfo member) {
        return ProjectResource.builder()
                .resourceNo(resource.getResourceNo())
                .project(project)
                .member(member)
                .resourceStartDate(LocalDate.parse(resourceStartDate))
                .resourceEndDate(LocalDate.parse(resourceEndDate))
                .resourceDuration(Long.parseLong(resourceDuration))
                .resourceProgress(Integer.parseInt(resourceProgress))
                .resourceHours(Double.parseDouble(resourceHours))
                .build();
    }
}
