package com.dflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@ToString
@Builder
@Entity
@Table(name="PROJECT_RESOURCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResource extends BaseEntity {
    @Id
    @Column(name="RESOURCE_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="PROJECT_NO", nullable = false)
    private ProjectInfo project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="MEMBER_NO")
    private MemberInfo member;        //투입인력 직원

    @Column(name="RESOURCE_START_DATE", nullable = false)
    private LocalDate resourceStartDate;        //투입 시작일

    @Column(name="RESOURCE_END_DATE", nullable = false)
    private LocalDate resourceEndDate;        //투입 종료일

    @Column(name="RESOURCE_DURATION", nullable = false)
    private Long resourceDuration;        //투입 기간

    @Column(name="RESOURCE_RATE", nullable = false, columnDefinition = "tinyint not null")
    private Integer resourceProgress;        //참여율

    @Column(name="RESOURCE_HOURS", nullable = false)
    private Double resourceHours;        //실질 수행 투입공수


}
