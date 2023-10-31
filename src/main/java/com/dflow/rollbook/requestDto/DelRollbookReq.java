package com.dflow.rollbook.requestDto;


import com.dflow.entity.Rollbook;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DelRollbookReq {

    List<Long> rollbooks;
    String rollbookFlag;

    public List<Rollbook> toEntity(){
        List<Rollbook> list = new ArrayList<>();
        for(Long rollbookNo : rollbooks){
            Rollbook rollbook = Rollbook.builder()
                    .rollbookNo(rollbookNo)
                    .rollbookFlag("N")
                    .build();

            list.add(rollbook);
        }
        return list;
    }

}
