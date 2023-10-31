package com.dflow.project.responseDto;

import com.dflow.entity.MemberInfo;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespProjMember {
    private Long memberNo;
    private String memberName;
    private String memberStaff;
    private Long staffNo;

    public static RespProjMember of(MemberInfo member) {
        return RespProjMember.builder()
                .memberNo(member.getMemberNo())
                .memberName(member.getMemberNameKr())
                .memberStaff(member.getStaff().getStaffName())
                .staffNo(member.getStaff().getStaffNo())
                .build();
    }
}
