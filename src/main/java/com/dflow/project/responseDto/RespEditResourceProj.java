package com.dflow.project.responseDto;

import com.dflow.entity.ProjectInfo;
import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespEditResourceProj {
    private Long projectNo;
    private String projectName;        //프로젝트 이름
    private Double projectManhour;        //프로젝트 계획 투입공수
    private Integer projectResources;        //프로젝트 계획 투입인력
    private List<RespEditResource> resources;

    //entity -> dto
    public static RespEditResourceProj of(ProjectInfo projectInfo) {
        return RespEditResourceProj.builder()
                .projectNo(projectInfo.getProjectNo())
                .projectName(projectInfo.getProjectName())
                .projectManhour(projectInfo.getProjectManhour())
                .projectResources(projectInfo.getProjectResources())
                .resources(RespEditResource.of(projectInfo.getResources()))
                .build();
    }
}
