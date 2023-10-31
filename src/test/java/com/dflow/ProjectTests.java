package com.dflow;

import com.dflow.entity.MemberInfo;
import com.dflow.entity.ProjectInfo;
import com.dflow.entity.ProjectResource;
import com.dflow.project.responseDto.RespProjDept;
import com.dflow.project.service.ProjectService;
import com.dflow.repository.DepartmentRepository;
import com.dflow.repository.MemberInfoRepository;
import com.dflow.repository.ProjectInfoRepository;
import com.dflow.repository.ProjectResourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
public class ProjectTests {
    @Autowired
    ProjectInfoRepository projectInfoRepository;
    @Autowired
    ProjectResourceRepository projectResourceRepository;
    @Autowired
    MemberInfoRepository memberInfoRepository;
    @Autowired
    ProjectService projectService;


//    @Test
//    void 프로젝트저장테스트() {
//        for(int i = 1; i < 101; i++) {
//            ProjectInfo info = new ProjectInfo();
//            info.setProjectName("프로젝트 테스트 " + i);
////        info.setProjectName("창원대 코드하우스 프로젝트");
//            info.setProjectType("SI");
//            info.setProjectDescription("코딩아카데미 프로젝트팀 연계 그룹웨어 개발");
//            info.setProjectStartDate(LocalDate.of(2023, 6, 1));
//            info.setProjectEndDate(LocalDate.of(2023, 8, 10));
//            info.setProjectState("진행중");
//            info.setProjectProgress(32);
//            info.setProjectResources(2 + i % 8);
//            info.setProjectManhour(info.getProjectResources() * 3.0);
//            info.setProjectOverview("창원대 코드하우스 기업연계 프로젝트 수행\n수요기반 기업주도형 셀 기반 프로젝트 운영으로 새로운 기업협력모델 도출 및 지역 내 IT/SW 개발을 위한 실무형 인력양성 및 지역기업의 SW인력 부족 문제 해결과 코딩아카데미 운영을 통한 지역 기업으로 취업 연계");
//            info.setProjectObjective("기존 자체 개발한 그룹웨어를 사용하고 있으나, 회사 규모 증가에 따른 기능 개선 필요성이 높아지고 체계적인 인력 자원 관리를 위한 프로젝트별 투입 공수 관리 기능이 포함된 그룹웨어 개발을 목표로 한다.");
//            info.setProjectFeatures("전자결재 - 기안자가 문서작성을 하고 등록한 문서를 검토 및 결재하여 처리하는 전자결재 관리\n" +
//                    "근태관리 - 출결기록 수행 및 확인하고, 연차내역 확인\n" +
//                    "프로젝트 - 프로젝트 정보를 입력 및 관리하고, 프로젝트별 투입인력관리\n" +
//                    "조직도 - 기업 조직 구성도 및 임직원 현황 확인\n" +
//                    "게시판 - 공지사항 게시판 및 개선요청사항 게시판\n" +
//                    "관리자 - 각 기능에 대한 관리 기능");
//            info.setCreateNo(1L);
//            info.setUpdateNo(1L);
//            projectInfoRepository.save(info);
//        }
//    }


//    @Test
//    void 프로젝트_공수인력_저장테스트() {
//        MemberInfo member = memberInfoRepository.findById(1L).get();
//        ProjectResource resource = new ProjectResource();
//        resource.setProject(projectInfoRepository.findById(1L).get());
//        resource.setMember(member);
//        resource.setResourceStartDate(LocalDate.of(2023, 6, 1));
//        resource.setResourceEndDate(LocalDate.of(2023, 10, 1));
//        resource.setResourceProgress(40);
//        resource.setCreateNo(1L);
//        resource.setUpdateNo(1L);
//        projectResourceRepository.save(resource);
//    }

    //프로젝트 투입공수 출력 확인
//    @Test
//    void 투입공수_출력() {
//        List<ProjectResource> list = projectResourceRepository.findProjectInfosByConditionsInMain(1L);
//        System.out.println("-----------test-----------");
//        for(ProjectResource resource : list)
//            System.out.println(resource.getProject().getProjectName());
//        System.out.println("-----------test-----------");
//    }

    //프로젝트에 들어간 투입공수 출력 확인
//    @Test
//    void 프로젝트_등록된_투입공수_테스트() {
//        ProjectInfo project = projectInfoRepository.findById(1L).get();
//        List<ProjectResource> resourceList = project.getResources();
//        long sum = 0;
//        System.out.println("-----------test-----------");
//        for(ProjectResource resource : resourceList) {
//            //투입기간
//            long duration = ChronoUnit.DAYS.between(resource.getResourceStartDate(), resource.getResourceEndDate()) + 1;
//            double manhour = duration * resource.getResourceProgress() / 100;
//
//            //출력부
//            String msg = "";
//            msg += "번호:" + resource.getResourceNo() + "\n";
//            msg += "팀원 이름:" + resource.getMember().getMemberNameKr() + "\n";
//            msg += "투입시작:" + resource.getResourceStartDate() + "\n";
//            msg += "투입종료:" + resource.getResourceEndDate() + "\n";
//            msg += "투입기간:" + duration + "\n";
//            msg += "참여율:" + resource.getResourceProgress() + "\n";
//            msg += "투입공수:" + manhour + "\n";
//            System.out.println(msg);
//        }
//        System.out.println("-----------test-----------");
//    }

    //투입공수 입력 안 된 프로젝트 목록 테스트
//    @Test
//    void 투입공수_등록용_프로젝트_목록_테스트() {
//        List<ProjectInfo> list = projectInfoRepository.findProjectInfoWithEmptyResources();
//        System.out.println("------------------test------------------");
//        for(ProjectInfo project : list) {
//            System.out.println("project name: " + project.getProjectName());
//        }
//        System.out.println("------------------test------------------");
//    }

    //부서 목록 테스트
//    @Test
//    void 부서목록테스트() {
//        List<RespProjDept> list = projectService.selResourceDepartmentList();
//        System.out.println("------------------------test------------------------");
//        for(RespProjDept dept : list) {
//            System.out.println(dept.toString());
//        }
//        System.out.println("------------------------test------------------------");
//    }
}
