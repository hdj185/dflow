package com.dflow.entity;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "ANNUAL_LEAVE")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnualLeave extends BaseEntity {

    @Id
    @Column(name = "ANNUAL_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long annualNo;  // 연차 고유번호

    @Column(name = "ANNUAL_COUNT", nullable = false)
    private Double annualCount;    // 연차수   (총 연차수)

    @Column(name = "ANNUAL_TYPE", nullable = false)
    private String annualType;    // 연차 유형 (기본, 특별) (안씀)

    @Column(name = "ANNUAL_END_DATE", nullable = false)
    private LocalDate annualEndDate;    // 연차 만료일

    @Column(name = "ANNUAL_LEFT", nullable = false)
    private Double annualLeft;             //남은 연차수

    @Column(name = "MEMBER_NO", nullable = false)
    private Long memberNo;    // 직원 고유번호

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_NO", referencedColumnName = "MEMBER_NO", insertable = false, updatable = false)
    private MemberInfo member;

}
