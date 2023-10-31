package com.dflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@ToString
@Entity
@Table(name = "ROLLBOOK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rollbook extends BaseEntity {

    @Id
    @Column(name = "ROLLBOOK_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rollbookNo;  // 근태 고유번호

    @Column(name = "ROLLBOOK_OPEN_STATE")
    private String rollbookOpenState;    // 출근 상태

    @Column(name = "ROLLBOOK_CLOSE_STATE")
    private String rollbookCloseState;    // 퇴근 상태

    @Column(name = "ROLLBOOK_OPEN_TIME")
    private LocalDateTime rollbookOpenTime;    // 출근 시간

    @Column(name = "ROLLBOOK_CLOSE_TIME")
    private LocalDateTime rollbookCloseTime;    // 퇴근 시간

    @Column(name = "ROLLBOOK_CONTENTS", columnDefinition = "TEXT")
    private String rollbookContents;    // 사유

    @Column(name = "ROLLBOOK_FLAG")
    private String rollbookFlag;       // 근태 사용 플래그 (Y: 사용, N: 삭제)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER_NO", nullable = false)
    private MemberInfo member;    // 직원 고유번호

    @OneToMany(mappedBy = "rollbook", fetch = FetchType.LAZY)
    private List<AdminRequest> corrections;




    public void setRollbookOpenState(String rollbookOpenState) {
        this.rollbookOpenState = rollbookOpenState;
    }

    public void setRollbookCloseState(String rollbookCloseState) {
        this.rollbookCloseState = rollbookCloseState;
    }

    public void setRollbookOpenTime(LocalDateTime rollbookOpenTime) {
        this.rollbookOpenTime = rollbookOpenTime;
    }

    public void setRollbookCloseTime(LocalDateTime rollbookCloseTime) {
        this.rollbookCloseTime = rollbookCloseTime;
    }

    public void setRollbookContents(String rollbookContents) {
        this.rollbookContents = rollbookContents;
    }

    public void setMember(MemberInfo member) {
        this.member = member;
    }
}
