package com.dflow.project.responseDto;

import com.dflow.entity.ProjectResource;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespEditResource {
    private Long resourceNo;
    private String memberName;  //팀원 이름
    private LocalDate resourceStartDate;        //투입 시작일
    private LocalDate resourceEndDate;        //투입 종료일
    private Long resourceDuration;        //투입 기간
    private Integer resourceProgress;        //참여율
    private Double resourceHours;        //실질 수행 투입공수

    public static RespEditResource of(ProjectResource entity) {
        return RespEditResource.builder()
                .resourceNo(entity.getResourceNo())
                .memberName(entity.getMember().getMemberNameKr())
                .resourceStartDate(entity.getResourceStartDate())
                .resourceEndDate(entity.getResourceEndDate())
                .resourceDuration(entity.getResourceDuration())
                .resourceProgress(entity.getResourceProgress())
                .resourceHours(entity.getResourceHours())
                .build();
    }

    public static List<RespEditResource> of(List<ProjectResource> entityList) {
        List<RespEditResource> list = new ArrayList<>();
        for(ProjectResource resource : entityList)
            list.add(of(resource));
        return list;
    }
}
