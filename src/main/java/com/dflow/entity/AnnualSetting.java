package com.dflow.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "ANNUAL_SETTING")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnualSetting {
    @Id
    @Column(name = "ANNUAL_SETTING_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long anuSetNo;   // 연차 기준정보 고유번호

    @Column(name = "ANNUAL_SETTING_RESET_DATE", nullable = false)
    private String anuSetResetDate;    // 연차 생성 일자

    @Column(name = "ANNUAL_SETTING_INCREMENT_YEAR", nullable = false)
    private Long anuSetIncrementYear;    // 가산 기준 연수   (년마다)

    @Column(name = "ANNUAL_SETTING_INCREMENT_COUNT", nullable = false)
    private Long anuSetIncrementCnt;    // 가산개수   (년마다)

    @Column(name = "ANNUAL_SETTING_DEFAULT_COUNT", nullable = false)
    private Long anuSetDefaultCnt;    // 기본 생성 수

    @Column(name = "ANNUAL_SETTING_MAX_COUNT", nullable = false)
    private Long anuSetMaxCnt;    // 최대 생성 수
}
