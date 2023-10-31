package com.dflow.dashboard.responseDto;

import com.dflow.entity.ProjectInfo;
import com.dflow.entity.ProjectResource;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespMainProject {
    private Long projectNo;
    private String projectName;
    private Long projectDeadline;
    private int projectProgress;

    //엔티티 -> DTO 변환
    public static RespMainProject of(ProjectResource resource) {
        ProjectInfo info = resource.getProject();
        return RespMainProject.builder()
                .projectNo(info.getProjectNo())
                .projectName(info.getProjectName())
                .projectDeadline(DAYS.between(LocalDate.now(), info.getProjectEndDate()))
                .projectProgress(info.getProjectProgress())
                .build();
    }

    //엔티티 리스트 -> DTO 리스트 변환
    public static List<RespMainProject> of(List<ProjectResource> entityList) {
        List<RespMainProject> list = new ArrayList<>();
        for(ProjectResource entity : entityList)
            list.add(of(entity));
        return list;
    }
}
