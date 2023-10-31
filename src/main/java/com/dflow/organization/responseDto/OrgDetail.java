package com.dflow.organization.responseDto;


import com.dflow.entity.MemberInfo;
import lombok.*;
import lombok.Builder;


import java.time.LocalDate;


/**
 * 상세 조회
 * **/


@Data
@AllArgsConstructor
@Builder
public class OrgDetail {

    private String memberNameKr;
    private String memberNameEn;
    private String memberNameCn;
    private LocalDate memberBirthdate;
    private String memberPhone;
    private String memberEmail;
    private String memberAddress;
    private String departmentName;
    private String staffName;
    private LocalDate memberEnableDate;
    private String memberGender;
    private Long imgNo;

    // dto로 변환
    public static OrgDetail toDto(MemberInfo memberInfo) {
        return OrgDetail.builder()
                .memberNameKr(memberInfo.getMemberNameKr())
                .memberNameEn(memberInfo.getMemberNameEn())
                .memberNameCn(memberInfo.getMemberNameCn())
                .memberBirthdate(memberInfo.getMemberBirthdate())
                .memberPhone(memberInfo.getMemberPhone())
                .memberEmail(memberInfo.getMemberEmail())
                .memberAddress(memberInfo.getMemberAddress())
                .staffName(memberInfo.getStaff().getStaffName())
                .departmentName(memberInfo.getDepartment().getDepartmentName())
                .memberEnableDate(memberInfo.getMemberEnableDate())
                .imgNo(memberInfo.getImgAttach() != null ? memberInfo.getImgAttach().getFileAttachNo() : null)
                .memberGender(memberInfo.getMemberGender()).build();
    }
}

