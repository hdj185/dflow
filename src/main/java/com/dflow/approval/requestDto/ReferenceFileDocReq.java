package com.dflow.approval.requestDto;

import com.dflow.entity.MemberInfo;
import com.dflow.entity.ReferenceFile;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceFileDocReq {

    /** 게시글 번호 **/
    private Long docNo;
    /** 첨부파일 번호 **/
    private Long attachFileNo;
    /** 생성자 번호 **/
    private Long createNo;
    /** 참조 유무 **/
    private boolean fileFlag;

    public ReferenceFileDocReq(Long docNo, Long fileAttachNo, MemberInfo member) {
        setDocNo(docNo);
        setAttachFileNo(fileAttachNo);
        setCreateNo(member.getMemberNo());
        setFileFlag(false);

    }

    public ReferenceFile toEntitiy(){
        return ReferenceFile.builder()
                .docAprvNo(this.docNo)
                .attachFileNo(this.attachFileNo)
                .createNo(this.createNo)
                .fileFlag(false)
                .build();
    }
}
