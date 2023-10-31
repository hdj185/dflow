package com.dflow.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "login_out_history")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class LoginOutHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logNo;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "user_agent")
    private String userAgent;
}
