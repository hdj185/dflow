package com.dflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@ToString
@Builder
@Entity
@Table(name="PROJECT_INFO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfo extends BaseEntity {
    @Id
    @Column(name="PROJECT_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectNo;

    @Column(name="PROJECT_NAME", nullable = false)
    private String projectName;        //프로젝트 이름

    @Column(name="PROJECT_TYPE", nullable = false)
    private String projectType;        //프로젝트 구분

    @Column(name="PROJECT_DESCRIPTION", nullable = false, columnDefinition = "TEXT")
    private String projectDescription;        //프로젝트 설명

    @Column(name="PROJECT_START_DATE", nullable = false)
    private LocalDate projectStartDate;        //프로젝트 시작일

    @Column(name="PROJECT_END_DATE", nullable = false)
    private LocalDate projectEndDate;        //프로젝트 종료일

    @Column(name="PROJECT_STATE", nullable = false)
    private String projectState;        //프로젝트 상태

    @Column(name="PROJECT_PROGRESS", nullable = false, columnDefinition = "tinyint not null")
    private Integer projectProgress;        //프로젝트 진행률

    @Column(name="PROJECT_MANHOUR", nullable = false)
    private Double projectManhour;        //프로젝트 계획 투입공수

    @Column(name="PROJECT_RESOURCES", nullable = false)
    private Integer projectResources;        //프로젝트 계획 투입인력

    @Column(name="PROJECT_OVERVIEW", nullable = false, columnDefinition = "TEXT")
    private String projectOverview;        //프로젝트 개요

    @Column(name="PROJECT_OBJECTIVE", nullable = false, columnDefinition = "TEXT")
    private String projectObjective;        //프로젝트 목적

    @Column(name="PROJECT_FEATURES", nullable = false, columnDefinition = "TEXT")
    private String projectFeatures;        //프로젝트 기능

    @Column(name="PROJECT_REMARKS")
    private String projectRemarks;        //프로젝트 비고

    @Column(name="PROJECT_IS_DELETED", nullable = false, columnDefinition = "bit(1) default 0")
    private Boolean projectIsDeleted;       //프로젝트 삭제 여부

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    private List<ProjectResource> resources;
}
