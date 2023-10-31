package com.dflow.entity;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;


@Entity
@Table(name = "ADMIN_REQUEST")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequest extends BaseEntity {
    @Id
    @Column(name = "REQUEST_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestNo;

    @Column(name = "REQUEST_TYPE", nullable = false)
    private String requestType;    // 요청 종류

    @Column(name = "REQUEST_CONTENT")
    private String requestContent;    // 요청 내용

    @Column(name = "REQUEST_FLAG")
    private String requestFlag;    // 요청 처리 여부

    @Column(name = "ROLLBOOK_NO")
    private Long rollbookNo;    // 근태 고유번호 (정정할 근태)




    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLLBOOK_NO", referencedColumnName = "ROLLBOOK_NO", insertable = false, updatable = false)
    private Rollbook rollbook;
}
