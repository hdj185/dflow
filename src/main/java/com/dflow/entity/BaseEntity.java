package com.dflow.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/** 23-8-18
 * 어디팅을 닫도록 하는 객체  // 얘가 하는 역할을 물려받는?
 * **/

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity{  // BaseTimeEntity 를 상속받으면 BaseEntity 로 4가지 컬럼을 쓸 수 있다
    // 이용자에 관한 부분
    @CreatedBy
    @Column(updatable = false, name = "CREATE_NO")
    private Long createNo;

    @LastModifiedBy // 엔티티 수정하는 사람을 감지
    @Column(name = "UPDATE_NO")
    private Long updateNo;
}
