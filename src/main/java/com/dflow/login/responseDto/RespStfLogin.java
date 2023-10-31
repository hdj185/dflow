package com.dflow.login.responseDto;

import com.dflow.entity.Staff;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespStfLogin {

    private Long staffNo;

    private String staffName;

    // entity -> dto
    public static RespStfLogin of(Staff staff) {

        return RespStfLogin.builder()
                .staffNo(staff.getStaffNo())
                .staffName(staff.getStaffName())
                .build();
    }

    // entity list -> dto list
    public static List<RespStfLogin> of(List<Staff> staff) {
        List<RespStfLogin> list = new ArrayList<>();
        for (Staff entity : staff)
            list.add(of(entity));

        return list;
    }




}
