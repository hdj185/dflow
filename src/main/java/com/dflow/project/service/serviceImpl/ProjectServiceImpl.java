package com.dflow.project.service.serviceImpl;

import com.dflow.entity.*;
import com.dflow.organization.responseDto.DepartStaffMemberResp;
import com.dflow.project.requestDto.*;
import com.dflow.project.responseDto.*;
import com.dflow.project.service.ProjectService;
import com.dflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {
    private final ProjectInfoRepository projectInfoRepository;
    private final ProjectResourceRepository projectResourceRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final DepartmentRepository departmentRepository;
    private final CodeInfoRepository codeInfoRepository;

    //순수 프로젝트 엔티티 List
    @Override
    public Page<ProjectInfo> selProjectInfoList(Pageable pageable, ReqProjSearch search) {
        Page<ProjectInfo> projectPage = projectInfoRepository.getProjectInfosByConditions(pageable, search);
        return projectPage;
    }

    //프로젝트 목록 List 뽑기
    @Override
    public Page<RespDetailProject> selProjectList(Pageable pageable, ReqProjSearch search) {
        return RespDetailProject.of(selProjectInfoList(pageable, search));
    }

    //프로젝트 디테일
    @Override
    public RespDetailProject selDetailProject(Long projectNo) {
        ProjectInfo projectInfo = projectInfoRepository.findById(projectNo).get();
        return RespDetailProject.of(projectInfo);
    }

    //투입공수 목록 List 뽑기
    @Override
    public Page<RespProjResource> selResourceProjList(Pageable pageable, ReqProjSearch search) {
        return RespProjResource.of(selProjectInfoList(pageable, search));
    }

    //모든 프로젝트 리스트 가져오기(투입공수 등록 시 사용)
    @Override
    public List<RespProject> selAllProjectList() {
        List<ProjectInfo> projectList = projectInfoRepository.findProjectInfoWithEmptyResources();
        return RespProject.of(projectList);
    }

    //프로젝트 등록
    @Override
    public ProjectInfo insProjectInfo(ReqRegProject project) {
        try {
            ProjectInfo savedProject = projectInfoRepository.save(project.toEntity());
            return savedProject; // 저장 성공 시 저장된 엔티티 반환
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 저장 실패 시 null 반환 또는 다른 실패 처리
        }
    }

    @Override
    public ProjectInfo udtProjectInfo(ReqEditProject project) {
        try {
            ProjectInfo entity = projectInfoRepository.findById(project.getProjectNo()).get();
            ProjectInfo savedProject = projectInfoRepository.save(project.toEntity(entity));
            return savedProject; // 저장 성공 시 저장된 엔티티 반환
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 저장 실패 시 null 반환 또는 다른 실패 처리
        }
    }

    @Override
    public boolean delProjectInfo(Long projectNo) {
        try {
            Optional<ProjectInfo> optionalProject = projectInfoRepository.findById(projectNo);
            if (optionalProject.isPresent()) {
                ProjectInfo project = optionalProject.get();
                project.setProjectIsDeleted(true);
                return true;
            }
            return false;
        } catch(Exception e) {
            return false;
        }
    }

    //투입공수 등록하는 메소드
    @Override
    public String insResourceList(ReqResourceList resourceList) {
        ProjectInfo project = projectInfoRepository.getReferenceById(resourceList.getProjectNo());
        List<ProjectResource> resources = new ArrayList<>();
        for(ReqResource reqResource : resourceList.getResources()) {
            Optional<MemberInfo> optional = memberInfoRepository.findByMemberNameKr(reqResource.getMemberName());
            //해당 이름을 가진 사람이 있다면
            if(optional.isPresent()) {
                MemberInfo member = optional.get();
                ProjectResource proResource = reqResource.toEntity(project, member);
                resources.add(proResource);
            } else { //이름을 가진 직원이 없을 때, 메시지 띄우기
                return reqResource.getMemberName() + " 이름을 가진 직원이 없습니다.";
            }
        }

        try {
            projectResourceRepository.saveAll(resources);
            return "투입공수 등록 완료하였습니다!";
        } catch (DataIntegrityViolationException ex) {
            return "등록 실패하였습니다.";
        } catch (Exception e) {
            return "등록 실패하였습니다.";
        }
    }

    //투입공수 수정하는 메소드
    @Override
    public String udtResourceList(ReqResourceList resourceList) {
        ProjectInfo project = projectInfoRepository.getReferenceById(resourceList.getProjectNo());
        List<ProjectResource> originalResources = project.getResources();
        List<ProjectResource> resources = new ArrayList<>();
        for(int i = 0; i < resourceList.getResources().size(); i++) {
            ReqResource reqResource = resourceList.getResources().get(i);
            Optional<MemberInfo> optional = memberInfoRepository.findByMemberNameKr(reqResource.getMemberName());
            //해당 이름을 가진 사람이 있다면
            if(optional.isPresent()) {
                MemberInfo member = optional.get();
                ProjectResource proResource = i < originalResources.size() ?
                        reqResource.toEntity(originalResources.get(i), project, member) :
                        reqResource.toEntity(project, member);
                resources.add(proResource);
            } else { //이름을 가진 직원이 없을 때, 메시지 띄우기
                return reqResource.getMemberName() + " 이름을 가진 직원이 없습니다.";
            }
        }

        //기존에 있던 투입공수 리스트가 더 많을 경우 - 남은 투입공수 삭제
        if(originalResources.size() > resources.size()) {
            List<Long> ids = new ArrayList<>();
            for(int i = resources.size(); i < originalResources.size(); i++) {
                ids.add(originalResources.get(i).getResourceNo());
            }
            projectResourceRepository.deleteAllByIdInQuery(ids);
        }

        try {
            projectResourceRepository.saveAll(resources);
            return "투입공수 수정 완료하였습니다!";
        } catch (DataIntegrityViolationException ex) {
            return "등록 실패하였습니다.";
        } catch (Exception e) {
            return "등록 실패하였습니다.";
        }
    }


    @Override
    public List<RespProjDept> selResourceDepartmentList() {
        List<Department> departemntEntityList = departmentRepository.findByDepartmentParentNoIsNullAndDepartmentFlag(false);


        // 부서 우선순위로 배치
        departemntEntityList.sort(
            Comparator.comparing(Department::getQueueValue, Comparator.nullsLast(Comparator.naturalOrder()))
        );

        List<RespProjDept> departments = RespProjDept.of(departemntEntityList);


        return departments;
    }

    //M/M 계산용 M 구하기
    @Override
    public Double selManhourMonth() {
        CodeInfo codeInfo = codeInfoRepository.findCodeInfoByCodeTypeAndCodeName("MONTH", "M").get();
        return Double.parseDouble(codeInfo.getCodeAccount());
    }

    //프로젝트 구분 코드 리스트 구하기
    @Override
    public List<RespProjCode> selProjCodeList(String codeType) {
        List<CodeInfo> codeInfoList = codeInfoRepository.findCodeInfoByCodeTypeAndCodeFlagFalse(codeType);
        return RespProjCode.of(codeInfoList);
    }

    //프로젝트 투입공수 수정 정보 호출
    @Override
    public RespEditResourceProj selEditResource(Long projectNo) {
        ProjectInfo project = projectInfoRepository.findById(projectNo).get();
        return RespEditResourceProj.of(project);
    }

}
