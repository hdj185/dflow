package com.dflow.admin.organization.requestDto;


import com.dflow.entity.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminMemberReq {

    private Long memberNo;
    private Long departmentNo;
    private String memberNameKr;
    private String departmentName;
    private String staffName;
    private String updateName;
    private Long updateNo;



    public static ModelMapper modelMapper = new ModelMapper();

    /** 23 - 8 - 17 **/
    public static AdminMemberReq of(MemberInfo memberInfo){
        return modelMapper.map(memberInfo, AdminMemberReq.class);
    }




}
