package com.dflow.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "PERSISTENCE_LOGINS")
@Getter
@NoArgsConstructor
public class PersistenceLogins {

    @Column(name = "MEMBER_ID", nullable = false)
    private String memberId;

    @Id
    @Column(name = "SERIES")
    private String series;

    @Column(name = "TOKEN",  nullable = false)
    private String token;

    @Column(name = "LAST_USED", nullable = false)
    private Timestamp lastUsed;

}
