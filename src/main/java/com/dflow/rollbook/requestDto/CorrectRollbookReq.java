package com.dflow.rollbook.requestDto;


import com.dflow.entity.AdminRequest;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//정정 요청 리퀘스트 dto
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CorrectRollbookReq {
    List<Long> rollbooks;
    String content;

    public List<AdminRequest> toEntity() {
        List<AdminRequest> list = new ArrayList<>();
        for(Long rollbookNo : rollbooks) {
            AdminRequest adminRequest = AdminRequest.builder()
                    .requestType("근태정정요청")
                    .requestContent(this.content)
                    .requestFlag("N")
                    .rollbookNo(rollbookNo)
                    .build();
            list.add(adminRequest);
        }
        return list;
    }
}
