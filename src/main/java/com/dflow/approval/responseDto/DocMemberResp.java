package com.dflow.approval.responseDto;

import com.dflow.entity.MemberInfo;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocMemberResp {

    private Long memberNo;
    private String memberName;
    private String departmentName;
    private String staffName;
    private Long signNo;

    public static DocMemberResp of(MemberInfo memberInfo) {
        return DocMemberResp.builder()
                .memberNo(memberInfo.getMemberNo())
                .memberName(memberInfo.getMemberNameKr())
                .departmentName(memberInfo.getDepartment().getDepartmentName())
                .staffName(memberInfo.getStaff().getStaffName())
                .signNo(memberInfo.getSignAttachNo())
                .build();
    }
}
