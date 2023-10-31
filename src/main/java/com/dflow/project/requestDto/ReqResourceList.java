package com.dflow.project.requestDto;

import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqResourceList {
    private List<ReqResource> resources;
    Long projectNo;
}
