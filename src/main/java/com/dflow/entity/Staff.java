package com.dflow.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Entity
@Table(name="STAFF")
@Getter
@Setter
@NoArgsConstructor
public class Staff extends BaseTimeEntity {
    @Id
    @Column(name="STAFF_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffNo;

    @Column(name="STAFF_NAME", nullable = false)
    private String staffName;   //직책명

    //    @Column(name="STAFF_FLAG")
//    private String staffFlag;      //사용여부

    @Column(name="CREATE_NO")
    private Long createNo;   // 생성자 No

    @Column(name="UPDATE_NO")
    private Long updateNo;   // 수정자 No
}
