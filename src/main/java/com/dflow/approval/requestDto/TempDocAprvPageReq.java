package com.dflow.approval.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempDocAprvPageReq {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 20;

    @Builder.Default
    private String direction = "desc";

    public Pageable getPageable(String...props){
        Sort.Direction sortDirection = this.direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        System.out.println("direction : "+sortDirection);
        return PageRequest.of(this.page -1, this.size, Sort.by(sortDirection, props));
    }



    public void changePage(int page) {this.page = page;}
    public void changeSize(int size) {this.size = size;}
}
