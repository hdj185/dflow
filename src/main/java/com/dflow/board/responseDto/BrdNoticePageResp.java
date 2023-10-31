package com.dflow.board.responseDto;

import com.dflow.board.requestDto.BrdNoticePageReq;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class BrdNoticePageResp<T> {

    private int page;
    private int size;
    private int total;

    //시작 페이지 번호
    private int start;
    //끝 페이지 번호
    private int end;
    //마지막 페이지 번호
    private int last;

    //이전 페이지의 존재 여부
    private boolean prev;
    //다음 페이지의 존재 여부

    private boolean next;

    private List<T> pageResponseList;

    @Builder(builderMethodName = "withAll")
    public BrdNoticePageResp(BrdNoticePageReq brdNoticePageReq, List<T> pageResponseList, int total){
        if(total <= 0){
            return;
        }

        this.page = brdNoticePageReq.getPage();
        this.size = brdNoticePageReq.getSize();

        this.total = total;
        this.pageResponseList = pageResponseList;

        this.end = (int)(Math.ceil(this.page/10.0))*10; //화면에서의 마지막 번호
        this.start = this.end-9;

        this.last = (int)(Math.ceil((total/(double)size))); //데이터의 개수를 계산한 마지막 페이지 번호

        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;

    }
}
