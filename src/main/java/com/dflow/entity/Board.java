package com.dflow.entity;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDate;

@ToString
@Entity
@Table(name="BOARD")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Board extends BaseEntity {
    @Id
    @Column(name="BOARD_NO", nullable = false, updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardNo;                                                                       //게시글 고유 번호

    @Column(name="BOARD_TITLE", nullable = false)
    private String boardTitle;                                                                  //게시글 제목

    @Column(name="BOARD_CONTENT", nullable = false, columnDefinition = "TEXT")
    private String boardContent;                                                                //게시글 내용

    @Column(name="BOARD_NOTICE")
    private String boardNotice;                                                                 //공지여부

    @Column(name="BOARD_NOTIDATE_START")
    private LocalDate boardNotiDateStart;                                                            //공지기간

    @Column(name="BOARD_NOTIDATE_END")
    private LocalDate boardNotiDateEnd;                                                            //공지기간

    @Column(name="BOARD_FLAG", nullable = false, columnDefinition = "bit(1) default 0")
    private Boolean boardFlag;                                                                  //삭제여부

    @Column(name = "CREATE_NO")
    private Long createNo;

    @Column(name = "UPDATE_NO")
    private Long updateNo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATE_NO",referencedColumnName = "MEMBER_NO", updatable = false, nullable = false, insertable = false)
    private MemberInfo createInfo;                                                              //작성자

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATE_NO",referencedColumnName = "MEMBER_NO",updatable = false, nullable = false, insertable = false)
    private MemberInfo updateInfo;                                                              //수정자

}
