package com.dflow.dashboard.responseDto;

import com.dflow.entity.MemberInfo;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespMainMemberInfo {

    private String memberName;
    private String departmentName;
    private String staffName;
    private Long imgNo;

    public static RespMainMemberInfo of(MemberInfo memberInfo) {
        return RespMainMemberInfo.builder()
                .memberName(memberInfo.getMemberNameKr())
                .departmentName(memberInfo.getDepartment().getDepartmentName())
                .staffName(memberInfo.getStaff().getStaffName())
                .imgNo(memberInfo.getImgAttachNo())
                .build();
    }
}
