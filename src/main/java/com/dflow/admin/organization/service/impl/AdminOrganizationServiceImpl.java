package com.dflow.admin.organization.service.impl;

import com.dflow.admin.organization.requestDto.AdminDepartmentCreateReq;
import com.dflow.admin.organization.responseDto.*;
import com.dflow.admin.organization.service.AdminOrganizationService;
import com.dflow.entity.*;
import com.dflow.login.CustomUser;
import com.dflow.organization.responseDto.DepartStaffMemberResp;
import com.dflow.organization.responseDto.DeptTreeResp;
import com.dflow.organization.responseDto.OrgDetail;
import com.dflow.project.requestDto.ReqResource;
import com.dflow.project.requestDto.ReqResourceList;
import com.dflow.repository.DepartmentRepository;
import com.dflow.repository.MemberInfoRepository;
import com.dflow.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.internal.util.Members;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class AdminOrganizationServiceImpl implements AdminOrganizationService {

    private final DepartmentRepository departmentRepository;

    private final MemberInfoRepository memberInfoRepository;

    private final StaffRepository staffRepository;


    /**
     * 부서 고유 번호 조회  아이디 찾기
     **/
    @Override
    public AdminDepartmentResp getAdminDepartNo(Long departmentNo) {

        Department department = departmentRepository.findById(departmentNo)
            .orElseThrow(EntityExistsException::new);

        return AdminDepartmentResp.adminDepartment(department);
    }


    /**
     * 모든 department 를  get 한다
     **/
    @Override
    public List<AdminDepartmentResp> getAllDepartment() {
        List<Department> allDepartments = departmentRepository.findAll();

        allDepartments.sort(
            Comparator.comparing(Department::getQueueValue, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Department::getDepartmentNo, Comparator.naturalOrder())
        );


        List<AdminDepartmentResp> adminDepartmentRespList = new ArrayList<>();

        // 각 departmentNo 을 가져오기 위해
        for (Department department : allDepartments) {
            AdminDepartmentResp adminDepartmentResp = AdminDepartmentResp.adminDepartment(department);
            adminDepartmentRespList.add(adminDepartmentResp);
        }

        return adminDepartmentRespList;
    }


    /**
     * 23-8-9
     * 부서 수정에 사용할 DepartmentNo 얻기
     */
    @Override
    public AdminUdtDepartment getUdtAdminDepartmentNo(Long departmentNo) {
        Department department = departmentRepository.findById(departmentNo).orElseThrow(EntityExistsException::new);


        return AdminUdtDepartment.getAdminDepartmentNo(department);

    }


    /***
     * 부서 정보 수정하기 위한 메서드
     *
     * */
    @Override
    public AdminUdtDepartment udtAdminDepartment(Long departmentNo, AdminUdtDepartment udtDepartment) {
        //업데이트할 부서 정보 가져오기
        Department departmentToUpdate = departmentRepository.findById(departmentNo).orElseThrow(EntityExistsException::new);

        // Dto로 부터 필드 업데이트 및 변경 값 설정
        departmentToUpdate.setDepartmentName(udtDepartment.getDepartmentName());
//        departmentToUpdate.setDepartmentParentNo(departmentToUpdate.getDepartmentParentNo());
        departmentToUpdate.setDepartmentDepth(udtDepartment.getDepartmentDepth());
        departmentToUpdate.setDepartmentFlag(udtDepartment.isDepartmentFlag());

        // 부모 부서 정보 업데이트
        if (udtDepartment.getDepartmentParentNo() == null) {
            departmentToUpdate.setDepartmentParentNo(null);
            departmentToUpdate.setDepartmentDepth(1);
        } else {
            Department parentDepartment = departmentRepository.findById(udtDepartment.getDepartmentParentNo()).orElse(null);
            if (parentDepartment != null) {
                // 부모의 넘버 전달
                departmentToUpdate.setDepartmentParentNo(parentDepartment);
                departmentToUpdate.setDepartmentDepth(2);
            }
        }

        //상위부서가 하위부서로 수정 되면 queueValue null 로 세팅
        if(departmentToUpdate.getDepartmentDepth() != 1){
            departmentToUpdate.setQueueValue(null);
        }

        Department udatedDepartment = departmentRepository.save(departmentToUpdate);
        System.out.println(udtDepartment + " 여기는 어드민서비스 ");

        //DTO 리턴
        return AdminUdtDepartment.toAdminUdtDepartmentDto(udatedDepartment);

    }

    /**
     * 23-8-11
     * 부서의 모든 정보를 가져오기 위한
     **/

    @Override
    public List<Department> findAll() {
        List<Department> departmentList = departmentRepository.findAll();

        return departmentList;
    }


    /**
     * 상위 부서의 부서 리스트를 리턴
     */
    @Override
    public List<AdminUdtDepartment> departmentParentNoList() {
        List<Department> dep = departmentRepository.findDepartmentByDepartmentParentNoIsNull();
        List<AdminUdtDepartment> parentDepartmentList = new ArrayList<>();

        for (Department department : dep) {
            AdminUdtDepartment adminUdtDepartment = AdminUdtDepartment.getAdminDepartmentNo(department);
            parentDepartmentList.add(adminUdtDepartment);

        }

        return parentDepartmentList;
    }


    /***
     *  하위 부서
     */
    public List<AdminUdtDepartment> departmentChildNoList() {
        List<Department> subDepartments = departmentRepository.findDepartmentByDepartmentParentNoIsNotNull();
        List<AdminUdtDepartment> adminUdtSubDepartments = new ArrayList<>();

        for (Department subDepartment : subDepartments) {
            AdminUdtDepartment adminUdtSubDepartment = AdminUdtDepartment.getAdminDepartmentNo(subDepartment);
            adminUdtSubDepartments.add(adminUdtSubDepartment);
        }
        System.out.println("----------------------------- 하위 부서여기다");

        return adminUdtSubDepartments;
    }


    public List<AdminUdtDepartment> getChild(Long no) {
        List<AdminUdtDepartment> result = new ArrayList<>();
        for (Department d : departmentRepository.findByDepartmentParentNo(no)) {
            System.out.println(d + " 여기는 뭐라고 하는거여");
            result.add(AdminUdtDepartment.getAdminDepartmentNo(d));
        }
        return result;
    }


    /***
     * 상위 부서 이름하고 같은 하위 부서 가져오기
     *
     * @return
     */
    public List<AdminUdtDepartment> getSameName() {

        List<AdminUdtDepartment> departmentChildList = new ArrayList<>();

        // 상위부서를 리턴해 저장 
        List<AdminUdtDepartment> departmentParentNoList = departmentParentNoList();
        System.out.println(departmentParentNoList + "상위부서 리스트");

        //  하위 부서 리턴
        List<AdminUdtDepartment> departmentChildNoList = departmentChildNoList();
        System.out.println(departmentChildNoList + "하위부서 리스트");

        // 상위 부서 목록 돌리면서 이름이 포함 된  하위 부서 찾기
        for (AdminUdtDepartment parentDepartment : departmentParentNoList) {


            //순회하면서 같은 이름 찾기
            for (AdminUdtDepartment adminUdtDepartment : departmentChildNoList) {
                if (adminUdtDepartment.getDepartmentName().contains(parentDepartment.getDepartmentName())) {
                    departmentChildList.add(adminUdtDepartment);

                    System.out.println(adminUdtDepartment.getDepartmentName() + "하위 부서 이름");
                    System.out.println(parentDepartment.getDepartmentName() + "상위부서 이름");
                    System.out.println("같은 이름 리스트 " + departmentChildList.toString());
                    System.out.println("같은 이름 리스트 " + departmentChildList);
                }

            }
        }
        return departmentChildList;
    }


    //

    /***
     * 부서 추가
     * 23-9-16 로직 추가
     * queueValue 에 + 1 해주는
     * @return
     */
    @Override
    public AdminDepartmentCreateReq createAdminDepartment(AdminDepartmentCreateReq createDepartment) {
        // 새로운 entity 생성
        Department departmentCreateEntity = new Department();
        // 현재 접속 중인 유저 정보 가져오기

        // Dto로 부터 필드 업데이트 및 변경 값 설정
        departmentCreateEntity.setDepartmentName(createDepartment.getDepartmentName());
        departmentCreateEntity.setDepartmentDepth(createDepartment.getDepartmentDepth());
        departmentCreateEntity.setDepartmentFlag(createDepartment.isDepartmentFlag());


        // 부모 부서 정보 업데이트
        if (createDepartment.getDepartmentParentNo() == null) {
            departmentCreateEntity.setDepartmentParentNo(null);
            departmentCreateEntity.setDepartmentDepth(1);
            departmentCreateEntity.setQueueValue(maxQueueValue().getQueueValue() + 1);
        } else {
            Department parentDepartment = departmentRepository.findById(createDepartment.getDepartmentParentNo()).orElse(null);
            if (parentDepartment != null) {
                // 부모의 넘버 전달
                departmentCreateEntity.setDepartmentParentNo(parentDepartment);
                departmentCreateEntity.setDepartmentDepth(2);
            }
        }


        System.out.println("찍어 보자 여기는 어디 " + departmentCreateEntity.getQueueValue());


        Department saveDepartmentEntity = departmentRepository.save(departmentCreateEntity);
        System.out.println(createDepartment + " 여기는 부서추가 어드민 서비스 ");



        //DTO 리턴
        return AdminDepartmentCreateReq.toAdminDepartmentCreateReq(saveDepartmentEntity);
    }

    /**
     * 부서 삭제
     **/
    public void deleteDepartment(Long departmentNo) {
        departmentRepository.deleteById(departmentNo);
    }


    /**
     * 조직원 조회
     **/
    public List<AdminDepartStaffResp> toAdminMemberList() {
        List<AdminDepartStaffResp> memberList = memberInfoRepository
            .findAllByOrderByStaffNoAsc()
            .stream().map(AdminDepartStaffResp::toAdminMemberList).collect(Collectors.toList());
        return memberList;
    }


    /**
     *  부서별 직원 리스트 직책별 오름차순 조회
     *  23-8-18
     * **/

    @Override
    public List<AdminMemberResp> getMemberInfoListByDeptNo(Long departmentNo) {

        List<AdminMemberResp> memberInfoList = memberInfoRepository
            .findAllByDepartmentNoOrderByStaffNo(departmentNo)
            .stream()
            .map(AdminMemberResp :: toDto)
            .collect(Collectors.toList());


        return memberInfoList;
    }

    /** 23-8-20
     *  조직원 수정을 위한
     * **/

    @Override
    public AdminMemberResp udtAdminMember(Long memberNo, AdminMemberResp udtAdminMember) {
        //업데이트할 멤버 정보 가져오기
        MemberInfo memberToUpdate = memberInfoRepository.findById(memberNo).orElseThrow(EntityExistsException::new);

        System.out.println("조직원 수정을 위한 이곳에 들어오나?" + udtAdminMember.getDepartmentNo());

        // Dto로 부터 필드 업데이트 및 변경 값 설정
        memberToUpdate.setMemberNameKr(udtAdminMember.getMemberNameKr());
        memberToUpdate.setMemberLeaveDate(udtAdminMember.getMemberLeaveDate());
        memberToUpdate.setDepartmentNo(udtAdminMember.getDepartmentNo()); // 번호의 이름을 세팅
        memberToUpdate.setStaffNo(udtAdminMember.getStaffNo());
        memberToUpdate.setMemberEnableDate(udtAdminMember.getMemberEnableDate());




        //  임직원 조회의 부서 수정
        if (udtAdminMember.getDepartmentNo() != null) {   // 부서 번호가 null 이 아니라면
            Department department = departmentRepository.findById(udtAdminMember.getDepartmentNo()).orElseThrow(EntityExistsException::new);

            if (department != null) {
                memberToUpdate.setDepartment(department);
                udtAdminMember.setDepartmentName(department.getDepartmentName());
            }
        }

        // MemberFlag 가 null 이거나 "0" 이면 "0" 으로 설정.
        if(udtAdminMember.getMemberFlag()==null || udtAdminMember.getMemberFlag().equals("0")) {
            memberToUpdate.setMemberFlag("0");
            // 퇴사 LeaveDate 평소엔 null 입력 되면 퇴사 직원 처리
            if(udtAdminMember.getMemberLeaveDate() != null){
                memberToUpdate.setMemberFlag("퇴사직원");
            }
        }
        System.out.println("memberFlag" + udtAdminMember.getMemberFlag());



        // 임직원 조회의 직책 수정
        if(udtAdminMember.getStaffNo()!= null){
            Staff staff = staffRepository.findById(udtAdminMember.getStaffNo()).orElse(null);
            if(staff != null){
                memberToUpdate.setStaff(staff);
                udtAdminMember.setStaffName(staff.getStaffName());
            }
        }

        MemberInfo adminUdtMember = memberInfoRepository.save(memberToUpdate);

        // 업데이트된 멤버 정보를 AdminMemberResp 객체로 변환
        AdminMemberResp adminUdtMemberResp = AdminMemberResp.toDto(adminUdtMember);




        //DTO 리턴
        return adminUdtMemberResp;
    }

    /** 임직원 수정에 사용할 멤버 조회 **/
    public AdminMemberResp getAdminMemberNo(Long memberNo){

        MemberInfo memberInfo = memberInfoRepository.findById(memberNo).orElseThrow(EntityExistsException::new);

        return AdminMemberResp.toDto(memberInfo);
    }

    /** 임직원 수정에 부서 리스트를 출력하기 위한
     *   23-8-20
     * **/
    public List<AdminUdtDepartment> adminMemberDepartList(){
        List<Department> adminDepartList = departmentRepository.findByDepartmentNoNotNullAndDepartmentFlag(false);

        List<AdminUdtDepartment> adminMemberRespList = new ArrayList<>();

        for (Department department : adminDepartList) {
            AdminUdtDepartment adminResp = AdminUdtDepartment.getAdminDepartmentNo(department);
            adminMemberRespList.add(adminResp);
        }

        return adminMemberRespList;
    }

    /** 임직원 수정에 직책 리스트를 출력하기 위한
     *   23-8-20
     * **/
    public List<AdminMemberResp> adminMemberStaffList() {
        List<Staff> adminStaffList = staffRepository.findAllByOrderByStaffNoAsc();
        List<AdminMemberResp> adminList = new ArrayList<>();
        for (Staff staff : adminStaffList) {
            AdminMemberResp adminResp = AdminMemberResp.toStaffDto(staff);
            adminList.add(adminResp);
        }
        return adminList;
    }

    /**
     * 23-9-15
     * 부서 위로 움직이기 위한 로직 추가
     * **/

    public String upDepartmentValue(Long departmentNo) {
        try {
            // 선택한 부서 조회
            Department selectDepartment =  departmentRepository.findById(departmentNo).orElseThrow();

            if(selectDepartment.getDepartmentDepth() != 1){
                throw new IllegalArgumentException("상위부서만 이동 가능합니다.");
            }


            // 1 계층만 이동, 상위부서만 이동 시킴

            System.out.println("selectDepartment 계층 확인 부분" + selectDepartment.getDepartmentDepth() +  " queueValue 는? " + selectDepartment.getQueueValue());

            // selectDepartment의 value 보다 1 작은 것을 찾는다
            Department changeDepartment = departmentRepository.findByQueueValue(selectDepartment.getQueueValue()-1);

            //  1번 부서 검증
            if(changeDepartment == null){
                departmentRepository.save(selectDepartment);
                return "제일 위에 위치한 부서 입니다.";
            } else{

                System.out.println("save전 윗부서 우선순위: " + changeDepartment.getQueueValue() + " " + "윗부서명 확인:  " + changeDepartment.getDepartmentName());

                // 선택한 ( 아래 부서의 queueValue 로 세팅 )
                changeDepartment.changeQueueValue(selectDepartment.getQueueValue());
                // 선택한 부서의 queueValue를  -1 ( 위로 이동 한다 )
                selectDepartment.changeQueueValue(selectDepartment.getQueueValue()-1);

                System.out.println("save한 뒤에 값 확인 부분  체인지 벨류 " + changeDepartment.getQueueValue() +  " " +changeDepartment.getDepartmentName() +  " select 벨류 는? " + selectDepartment.getQueueValue() + selectDepartment.getDepartmentName());
            }

            //하위 부서 이동

            return "SUCCESS";
        } catch (Exception e){
            e.printStackTrace();
            return  e.getMessage(); // 실패 시 오류 메시지 반환
        }
    }

    /**
     * 23-9-16 부서 아래로 로직 추가
     * **/
    public String downDepartmentValue(Long departmentNo) {
        Department selectDepartment =  departmentRepository.findById(departmentNo).orElseThrow();

        try {

            if(selectDepartment.getDepartmentDepth() != 1){
                throw new IllegalArgumentException("상위부서만 이동 가능합니다.");
            }

            // 최하위 1계층 부서 찾기
            Department maxDepartment = maxQueueValue();
            if (selectDepartment.getQueueValue() == maxDepartment.getQueueValue()) {
                throw new IllegalArgumentException("가장 하위 부서 입니다.");
            }
            System.out.println("selectDepartment 계층 확인 부분" + selectDepartment.getDepartmentDepth() +  " queueValue 는? " + selectDepartment.getQueueValue());

            //바꿀 부서보다 아래 위치한 부서를 가져온다
            Department changeDepartmentList = departmentRepository.findByQueueValue(selectDepartment.getQueueValue()+1);
            Department changeDepartment = changeDepartmentList;
            System.out.println("save전 아랫부서 queueValue 확인" + changeDepartment.getQueueValue() + " " + "아랫부서명 확인  " + changeDepartment.getDepartmentName());

            // 아래 위치한 부서를 선택한 부서의 위치로 선택
            changeDepartment.changeQueueValue(selectDepartment.getQueueValue());

            // 선택한 부서의 queueValue를  +1 ( 아래로 이동 한다 )
            selectDepartment.changeQueueValue(selectDepartment.getQueueValue() +1);

            departmentRepository.save(selectDepartment);
            departmentRepository.save(changeDepartment);
            System.out.println("save한 뒤에 값 확인 부분  체인지 벨류 " + changeDepartment.getQueueValue() + " " + changeDepartment.getDepartmentName() + " select 벨류 는? " + selectDepartment.getQueueValue() + selectDepartment.getDepartmentName());


            return "SUCCESS";
        } catch (Exception e){
            e.printStackTrace();
            return  e.getMessage(); // 실패 시 오류 메시지 반환
        }
    }

    /**
     * 최하위 부서 찾기 위한 로직
     * **/
    public Department maxQueueValue(){

        Department maxValue = departmentRepository.findFirstByOrderByQueueValueDesc().orElseThrow(EntityExistsException::new);

        return maxValue;

    }


    //  상위부서 밑에 자식들을 따라 가게 하기 위한 코드
    public List<Department> getQueueParent() {
        // 상위부서가 null인 부서들을 가져옵니다.
        List<Department> topDepartments = departmentRepository.findDepartmentByDepartmentParentNoIsNull();

        // 결과를 담을 리스트입니다.
        List<Department> result = new ArrayList<>();

        for (Department department : topDepartments) {
            // 각 최상위 부서에서 시작하여 하위부서들을 재귀적으로 탐색하고 결과 리스트에 추가합니다.
            setChildQueue(department, result);

        }
        result.stream().forEach(department ->
            log.info("departmentNo: {}, departmentName: {}, queueValue: {}, childQueueValue: {}",
                department.getDepartmentNo(),
                department.getDepartmentName(),
                department.getQueueValue(),
                department.getChildQueueValue())
        );

        return result;
    }

    // 자식 우선순위 설정 하는 부분
    private void setChildQueue(Department currentDepart, List<Department> result){
        // 현재 부서 결과 추가
        result.add(currentDepart);

        //현재 부서의 자식부서들을 가져와 childQueueValue 순으로 정렬
        List<Department> childDepart = new ArrayList<>(currentDepart.getChildren());

        childDepart.sort(Comparator.comparing(Department::getChildQueueValue));

        for(Department child : childDepart){
            // 각 자식부서의 하위 부서를 찾는 재귀 탐색
            setChildQueue(child, result);
        }
    }
}

