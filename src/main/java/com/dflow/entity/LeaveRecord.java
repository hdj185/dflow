package com.dflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Entity
@Table(name = "LEAVE_RECORD")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRecord extends BaseEntity {

    @Id
    @Column(name = "LEAVE_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveNo;  // 연차기록 고유번호

    @Column(name = "LEAVE_START_DATE")
    private LocalDate leaveStartDate;       // 연차 시작일

    @Column(name = "LEAVE_END_DATE")
    private LocalDate leaveEndDate;         // 연차 종료일

    @Column(name = "LEAVE_TYPE", nullable = false)
    private String leaveType;    // 휴가 구분(반차, 반반차, 연차, 휴가 등)

    @Column(name = "LEAVE_CONTENTS", nullable = false, columnDefinition = "TEXT")
    private String leaveContents;    // 연차 사유

    @Column(name = "LEAVE_USE_COUNT", nullable = false)
    private Double leaveUseCount;    // 연차 차감 일수

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER_NO", nullable = false)
    private MemberInfo member;    // 직원 고유번호

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DOCUMENT_NO", nullable = false)
    private DocumentApproval document;  //결재 문서
}
