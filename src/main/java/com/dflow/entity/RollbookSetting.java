package com.dflow.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ToString
@Entity
@Table(name="ROLLBOOK_SETTING")
@Getter
@Setter
@NoArgsConstructor
public class RollbookSetting extends BaseEntity {

    @Id
    @Column(name = "SETTING_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settingNo;  // 근태설정 고유번호

    @Column(name = "SETTING_OPEN_TIME")
    private LocalTime settingOpenTime;    // 출근시간

    @Column(name = "SETTING_CLOSE_TIME")
    private LocalTime settingCloseTime;    // 퇴근시간

}
