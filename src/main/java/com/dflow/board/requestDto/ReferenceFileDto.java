package com.dflow.board.requestDto;

import com.dflow.entity.MemberInfo;
import com.dflow.entity.ReferenceFile;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceFileDto {

    /** 게시글 번호 **/
    private Long boardNo;
    /** 첨부파일 번호 **/
    private Long attachFileNo;
    /** 생성자 번호 **/
    private Long createNo;
    /** 참조 유무 **/
    private boolean fileFlag;

    public ReferenceFileDto(Long boardNo, Long fileAttachNo, MemberInfo member) {
        setBoardNo(boardNo);
        setAttachFileNo(fileAttachNo);
        setCreateNo(member.getMemberNo());
        setFileFlag(false);

    }

    public ReferenceFile toEntitiy(){
        return ReferenceFile.builder()
                .boardNo(this.boardNo)
                .attachFileNo(this.attachFileNo)
                .createNo(this.createNo)
                .fileFlag(false)
                .build();
    }
}
