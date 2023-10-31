package com.dflow.project.responseDto;

import lombok.*;
import org.springframework.data.domain.Page;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespPage {
    private int nowPage;
    private int startPage;  //첫 페이지
    private int endPage;    //마지막 페이지
    private int firstPage;  //페이징 버튼으로 나타낼 첫 페이지
    private int lastPage;   //페이징 버튼으로 나타낼 마지막 페이지

    public RespPage(Page<?> page) {
        int pagingLen = 9; //한번에 나타낼 페이징 버튼 수
        setNowPage(page.getPageable().getPageNumber() + 1);
        setStartPage(1);
        setEndPage(page.getTotalPages());

        //실제 페이지 수가 세팅된 pagingLen보다 작을 때
        if(pagingLen > endPage)
            pagingLen = endPage;

        // 페이징 버튼으로 나타낼 첫 페이지와 마지막 페이지 계산
        if (endPage <= pagingLen) {
            setFirstPage(startPage);
            setLastPage(endPage);
        } else if (nowPage <= pagingLen / 2 + 1) {
            setFirstPage(startPage);
            setLastPage(pagingLen);
        } else if (nowPage >= endPage - pagingLen / 2) {
            setFirstPage(endPage - pagingLen + 1);
            setLastPage(endPage);
        } else {
            setFirstPage(nowPage - pagingLen / 2);
            setLastPage(nowPage + pagingLen / 2);
        }
    }
}
