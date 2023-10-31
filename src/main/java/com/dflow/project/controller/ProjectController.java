package com.dflow.project.controller;

import com.dflow.entity.ProjectInfo;
import com.dflow.project.requestDto.ReqEditProject;
import com.dflow.project.requestDto.ReqRegProject;
import com.dflow.project.requestDto.ReqResourceList;
import com.dflow.project.requestDto.ReqProjSearch;
import com.dflow.project.responseDto.*;
import com.dflow.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/project")
public class ProjectController {
    private final ProjectService projectService;

    //프로젝트 목록 조회
    @GetMapping("/selProjectMain")
    public String selProjectMain(Model model,
                                 ReqProjSearch searchInfo,
                                 @RequestParam(defaultValue = "1", required = false) Integer page,
                                 @RequestParam(name = "sortType", defaultValue = "desc") String sortType,
                                 @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                 @PageableDefault(size = 20, sort = "projectNo", direction = Sort.Direction.DESC) Pageable pageable) {
        List<RespProjCode> typeCodes = projectService.selProjCodeList("PROJ_CODE");
        List<RespProjCode> stateCodes = projectService.selProjCodeList("PROJ_STATE");
        model.addAttribute("typeCodes", typeCodes);  //구분 코드 리스트
        model.addAttribute("stateCodes", stateCodes);  //진행상태 코드 리스트

        Sort sort = sortType.equals("asc") ? Sort.by("projectNo") : Sort.by("projectNo").descending();
        pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<RespDetailProject> projectPage = projectService.selProjectList(pageable, searchInfo);
        RespPage pageInfo = new RespPage(projectPage);
        model.addAttribute("projectList", projectPage.getContent());  //리스트 정보
        model.addAttribute("totalCount", projectPage.getTotalElements());    //총 데이터 건수
        model.addAttribute("pageInfo", pageInfo);       //페이징 정보
        model.addAttribute("searchInfo", searchInfo);   //검색 정보
        model.addAttribute("sortType", sortType);       //정렬 타입(최신순, 과거순)
        model.addAttribute("pageSize", pageSize);       //페이지수
        return "project/project_main_list";
    }

    //프로젝트 상세 조회 페이지
    @GetMapping("/selProjectMainDetail/{projectNo}")
    public String selProjectMainDetail(Model model,
                                       @PathVariable("projectNo") Long projectNo) {
        RespDetailProject project = projectService.selDetailProject(projectNo);
        model.addAttribute("project", project);
        return "project/project_main_detail";
    }

    //프로젝트 등록 페이지
    @GetMapping("/insProjectMain")
    public String insProjectMain(Model model) {
        List<RespProjCode> typeCodes = projectService.selProjCodeList("PROJ_CODE");
        List<RespProjCode> stateCodes = projectService.selProjCodeList("PROJ_STATE");
        model.addAttribute("typeCodes", typeCodes);  //구분 코드 리스트
        model.addAttribute("stateCodes", stateCodes);  //진행상태 코드 리스트
        return "project/project_main_sub";
    }

    //프로젝트 등록 요청
    @ResponseBody
    @PostMapping("/insProjectMain")
    public ResponseEntity<String> insProjectMainRequest(@RequestBody ReqRegProject project) {
        ProjectInfo result = projectService.insProjectInfo(project);
        if(result != null) {
            return ResponseEntity.ok("프로젝트 저장 완료하였습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("프로젝트 저장에 실패하였습니다.");
        }
    }

    //프로젝트 수정 페이지
    @GetMapping("/udtProjectMain/{projectNo}")
    public String udtProjectMain(Model model,
                                 @PathVariable("projectNo") Long projectNo) {
        List<RespProjCode> typeCodes = projectService.selProjCodeList("PROJ_CODE");
        List<RespProjCode> stateCodes = projectService.selProjCodeList("PROJ_STATE");
        model.addAttribute("typeCodes", typeCodes);  //구분 코드 리스트
        model.addAttribute("stateCodes", stateCodes);  //진행상태 코드 리스트

        RespDetailProject project = projectService.selDetailProject(projectNo);
        model.addAttribute("project", project);  //프로젝트 정보
        return "project/project_main_edit";
    }

    //프로젝트 수정 요청
    @ResponseBody
    @PostMapping("/udtProjectMain")
    public ResponseEntity<String> udtProjectMainRequest(@RequestBody ReqEditProject project) {
        ProjectInfo result = projectService.udtProjectInfo(project);
        if(result != null) {
            return ResponseEntity.ok("프로젝트 수정 완료하였습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("프로젝트 저장에 실패하였습니다.");
        }
    }

    //프로젝트 삭제 요청
    @DeleteMapping("/delProjectMain/{projectNo}")
    public ResponseEntity<String> delProjectMain(@PathVariable("projectNo") Long projectNo) {
        boolean result = projectService.delProjectInfo(projectNo);
        if(result) {
            return ResponseEntity.ok("프로젝트 삭제 완료하였습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("프로젝트 삭제에 실패하였습니다.");
        }
    }


    //투입공수 목록 조회
//    @ResponseBody
    @GetMapping("/selResource")
    public String selResource(Model model,
                              ReqProjSearch searchInfo,
                              @RequestParam(defaultValue = "1", required = false) Integer page,
                              @RequestParam(name = "sortType", defaultValue = "desc") String sortType,
                              @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                              @PageableDefault(size = 20, sort = "projectNo", direction = Sort.Direction.DESC) Pageable pageable) {
        List<RespProjCode> typeCodes = projectService.selProjCodeList("PROJ_CODE");
        List<RespProjCode> stateCodes = projectService.selProjCodeList("PROJ_STATE");
        model.addAttribute("typeCodes", typeCodes);  //구분 코드 리스트
        model.addAttribute("stateCodes", stateCodes);  //진행상태 코드 리스트

        Sort sort = sortType.equals("asc") ? Sort.by("projectNo").ascending() : Sort.by("projectNo").descending();
        pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<RespProjResource> projResourcePage = projectService.selResourceProjList(pageable, searchInfo);
        RespPage pageInfo = new RespPage(projResourcePage);
        model.addAttribute("projResourceList", projResourcePage.getContent());  //리스트 정보
        model.addAttribute("totalCount", projResourcePage.getTotalElements());    //총 데이터 건수
        model.addAttribute("pageInfo", pageInfo);       //페이징 정보
        model.addAttribute("searchInfo", searchInfo);   //검색 정보
        model.addAttribute("sortType", sortType);       //정렬 타입(최신순, 과거순)
        model.addAttribute("pageSize", pageSize);       //페이지수
        return "project/project_resource_list";
    }

    //투입공수 등록 페이지
    @GetMapping("/insResource")
    public String insResourceResp(Model model) {
        List<RespProject> projectList = projectService.selAllProjectList();
        model.addAttribute("projectList", projectList);

        List<RespProjDept> departmentList = projectService.selResourceDepartmentList();
        model.addAttribute("departmentList", departmentList);

        Double month = projectService.selManhourMonth();
        model.addAttribute("month", month);
        return "project/project_resource_sub";
    }

    //투입공수 등록 요청
    @ResponseBody
    @PostMapping("/insResourceReq")
    //ResponseEntity<String>
    public String insResourceReq(@RequestBody ReqResourceList resourceList) {
        return projectService.insResourceList(resourceList);
    }

    //투입공수 수정 페이지
    @GetMapping("/udtResource/{projectNo}")
    public String udtResource(@PathVariable("projectNo") Long projectNo, Model model) {
        //프로젝트&투입공수 정보 넘기기
        RespEditResourceProj project = projectService.selEditResource(projectNo);
        model.addAttribute("project", project);

        //부서 정보
        List<RespProjDept> departmentList = projectService.selResourceDepartmentList();
        model.addAttribute("departmentList", departmentList);

        //M값 넘기기
        Double month = projectService.selManhourMonth();
        model.addAttribute("month", month);
        return "project/project_resource_edit";
    }

    //투입공수 수정 요청
    @ResponseBody
    @PostMapping("/udtResource")
    //ResponseEntity<String>
    public String udtResourceReq(@RequestBody ReqResourceList resourceList) {
        return projectService.udtResourceList(resourceList);
    }
}
