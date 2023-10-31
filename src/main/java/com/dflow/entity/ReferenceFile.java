package com.dflow.entity;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@ToString
@Entity
@Table(name="REFERENCE_FILE")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReferenceFile extends BaseTimeEntity{

    @Id
    @Column(name="REFERENCE_FILE_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long referenceFileNO;

    @Column(name = "BOARD_NO")
    private Long boardNo;

    @Column(name = "ATTACH_FILE_NO")
    private Long attachFileNo;

    @Column(name = "CREATE_NO")
    private  Long createNo;

    @Column(name = "FILE_FLAG")
    private boolean fileFlag;

    @Column(name = "DOCUMENT_APPROVAL_NO")
    private Long docAprvNo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ATTACH_FILE_NO", insertable = false, updatable = false)
    private AttachFile attachFile;

    public void changeFileFlag(boolean fileFlag) {this.fileFlag = fileFlag;}



}
