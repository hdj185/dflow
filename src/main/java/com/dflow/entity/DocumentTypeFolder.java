package com.dflow.entity;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
@Table(name = "document_type_folder", schema = "dflow")
public class DocumentTypeFolder extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TYPE_FOLDER_NO", nullable = false)
    private Long typeFolderNo;                                                  //양식폴더 번호

    @Column(name = "TYPE_FOLDER_NAME", nullable = false)
    private String typeFolderName;                                              //양식폴더 명

    @Column(name = "PARENT_FOLDER_NO", insertable = true, updatable = false)
    private Long parentFolderNo;                                                //상위 문서 번호


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_FOLDER_NO", insertable = false, updatable = false)
    private DocumentTypeFolder parentFolder;                                    //상위 문서


    @Column(name = "DEPTH", nullable = false)
    private int depth;                                                          //폴더 위치

    @Column(name = "TYPE_FOLDER_FLAG", nullable = false)
    private String typeFolderFlag;                                              // 사용여부

    @Column(name = "ORDER_VALUE")
    private Integer orderValue;

    @OneToMany(mappedBy = "parentFolder")
    @OrderBy("orderValue asc")
    private List<DocumentTypeFolder> childrenFolder = new ArrayList<>();        //하위문서 리스트

    @OneToMany(mappedBy = "typeFolder")
    @OrderBy("orderValue asc")
    private List<DocumentType> typeList = new ArrayList<>();

    public void changeTypeFolderName(String typeFolderName){this.typeFolderName = typeFolderName;}
    public void changeTypeFolderOrderValue(Integer orderValue){this.orderValue = orderValue;}
    public void changeTypeFolderFlag(String typeFolderFlag) { this.typeFolderFlag = typeFolderFlag; }
}
