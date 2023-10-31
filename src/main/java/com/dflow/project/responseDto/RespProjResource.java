package com.dflow.project.responseDto;

import com.dflow.entity.MemberInfo;
import com.dflow.entity.ProjectInfo;
import com.dflow.entity.ProjectResource;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//투입공수 목록에 쓸 dto
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespProjResource {
    private Long projNo;            //프로젝트 번호
    private String projName;        //프로젝트 이름
    private String projType;        //프로젝트 구분
    private String projMembers;     //투입인력 목록(사람 이름 목록)
    private Integer plannedResources;       //계획 투입인력
    private Double plannedManhour;          //계획 투입공수
    private Integer performedResources;     //수행 투입인력
    private Double performedManhour;        //수행 투입공수
    private Double manhourDefference;       //투입공수 차이

    //entity -> dto
    public static RespProjResource of(ProjectInfo project) {
        List<ProjectResource> resourceList = project.getResources();
        String members = "";
        long manhourSum = 0;
        for(int i = 0; i < resourceList.size(); i++) {
            ProjectResource resource = resourceList.get(i);
            members += (i > 0 ? ", " : "") + resource.getMember().getMemberNameKr(); //투입인력 인원 목록
            manhourSum += resource.getResourceHours() * 100;  //수행 투입공수 * 100 합계
        }


        Long diffence = manhourSum - Math.round(project.getProjectManhour() * 100);

        return RespProjResource.builder()
                .projNo(project.getProjectNo())
                .projName(project.getProjectName())
                .projType(project.getProjectType())
                .projMembers(members)
                .plannedResources(project.getProjectResources())
                .plannedManhour(project.getProjectManhour())
                .performedResources(resourceList.size())
                .performedManhour(manhourSum / 100D)
                .manhourDefference(diffence / 100D)
                .build();
    }

    //Page<entity> -> Page<dto>
    public static Page<RespProjResource> of(Page<ProjectInfo> entityList) {
        List<RespProjResource> list = entityList.getContent().stream()
                .map(RespProjResource::of)
                .collect(Collectors.toList());
        return new PageImpl<>(list, entityList.getPageable(), entityList.getTotalElements());
    }
}
