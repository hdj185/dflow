package com.dflow.entity;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DEPARTMENT")
@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
public class Department extends BaseEntity {

    @Id
    @Column(name = "DEPARTMENT_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentNo;

    @Column(name = "DEPARTMENT_NAME", nullable = false)
    private String departmentName; // 부서명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_PARENT_NO", referencedColumnName = "DEPARTMENT_NO")
    private Department departmentParentNo; // 상위 부서 no

    @Column(name = "DEPARTMENT_DEPTH")
    private Integer departmentDepth; // 계층

    @Column(name = "QUEUE_VALUE")
    private Long queueValue; // 우선순위 큐

    @Column(name = "CHILD_QUEUE_VALUE")
    private Long childQueueValue; // 자식 우선순위

    @Column(name = "DEPARTMENT_FLAG", nullable = false, columnDefinition = "bit(1) default 0")
    private boolean departmentFlag; // 사용여부

//    @Column(name = "CREATE_NO")
//    private Long createNo; // 생성자 번호
//
//    @Column(name = "UPDATE_NO")
//    private Long updateNo; // 수정자 번호

    @OneToMany(mappedBy = "departmentParentNo")
    private List<Department> children = new ArrayList<>(); // 디파트 먼트 넘버를 갖고 있는애들 호출

    @OneToMany(mappedBy = "department")
    private List<MemberInfo> members = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATE_NO", referencedColumnName = "MEMBER_NO", insertable = false, updatable = false)
    // 중복 컬럼 매핑 문제 해결 insertable, updatable
    private MemberInfo createInfo;

//    public void changeTypeFolderName(String typeFolderName){this.typeFolderName = typeFolderName;}
    public void changeQueueValue(Long queueValue){this.queueValue = queueValue;}
    public void changeChildQueueValue(Long childQueueValue){this.childQueueValue = childQueueValue;}
    public void changeDepartmentFlag(boolean departmentFlag) { this.departmentFlag = departmentFlag; }

}


