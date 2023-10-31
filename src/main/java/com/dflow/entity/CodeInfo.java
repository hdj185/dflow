package com.dflow.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="CODE_INFO")
@Getter
@Setter
@NoArgsConstructor
public class CodeInfo extends BaseTimeEntity {
    @Id
    @Column(name="CODE_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeNo;            //코드 고유번호

    @Column(name="CODE_TYPE", nullable = false)
    private String codeType;        //코드 타입

    @Column(name="CODE_NAME", nullable = false)
    private String codeName;        //물리 코드명

    @Column(name="CODE_ACCOUNT", nullable = false)
    private String codeAccount;     //논리 코드명

    @Column(name="CODE_VALUE", columnDefinition = "TEXT")
    private String codeValue;       //코드 보유값

    @Column(name="CODE_FLAG", columnDefinition = "bit(1) default 0")
    private Boolean codeFlag;       //코드 활성화 여부

    @Column(name="CREATE_NO")
    private Long createNo;        //생성자

    @Column(name="UPDATE_NO")
    private Long updateNo;        //수정자
}
