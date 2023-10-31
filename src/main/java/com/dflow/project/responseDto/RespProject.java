package com.dflow.project.responseDto;

import com.dflow.entity.ProjectInfo;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespProject {
    private Long projectNo;
    private String projectName;     //프로젝트 이름
    private String projectType;     //프로젝트 구분
    private Double projectManhour;  //프로젝트 투입공수(계획)
    private int projectResources;   //프로젝트 투입인력(계획)

    //entity -> dto
    public static RespProject of(ProjectInfo entity) {
        return RespProject.builder()
                .projectNo(entity.getProjectNo())
                .projectName(entity.getProjectName())
                .projectType(entity.getProjectType())
                .projectManhour(entity.getProjectManhour())
                .projectResources(entity.getProjectResources())
                .build();
    }

    //List<entity> -> List<dto>
    public static List<RespProject> of(List<ProjectInfo> entityList) {
        List<RespProject> list = new ArrayList<>();
        for(ProjectInfo entity : entityList)
            list.add(of(entity));
        return list;
    }
}
