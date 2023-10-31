package com.dflow.rollbook.service.impl;

import com.dflow.admin.approval.responseDto.AprvCodeResp;
import com.dflow.admin.organization.responseDto.AdminUdtDepartment;
import com.dflow.approval.responseDto.DocAprvSelResp;
import com.dflow.entity.*;
import com.dflow.repository.*;
import com.dflow.rollbook.requestDto.*;
import com.dflow.rollbook.responseDto.*;
import com.dflow.rollbook.service.RollbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.parse;

@Service
@RequiredArgsConstructor
@Transactional
public class RollbookServiceImpl implements RollbookService {

    private final RollbookRepository rollbookRepository;
    private final RollbookSettingRepository rollbookSettingRepository;
    private final AnnualLeaveRepository annualLeaveRepository;
    private final LeaveRecordRepository leaveRecordRepository;
    private final CodeInfoRepository codeInfoRepository;
    private final AdminRequestRepository adminRequestRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final AnnualSettingRepository annualSettingRepository;

    //연차 생성 기준 정보 받아오기
    @Override
    public AdminAnuSetResp selAdminAnuSetResp() {
        List<AnnualSetting> list = annualSettingRepository.findAll();
        return AdminAnuSetResp.of(list.get(0));
    }

    //연차 생성 기준 정보 update
    @Override
    public Boolean udtAdminAnnaulSetting(AdminAnuSetUdtReq request) {
        try {
            return annualSettingRepository.save(request.toEntity()) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<AprvCodeResp> selRollbookCodeList() {
        List<CodeInfo> codeInfoList = codeInfoRepository.findCodeInfoByCodeTypeAndCodeFlagFalse("ROLLBOOK_CODE");
        return AprvCodeResp.of(codeInfoList);
    }

    // 근태 기록 조회 -> 페이징 교채
    public List<RespRollbookList> selRollbookList(String memberId) {

        List<Rollbook> rollbookList = rollbookRepository.findAllByMemberId(memberId, "Y");
        List<RespRollbookList> respRollbookList = new ArrayList<>();

        for (Rollbook rollbook : rollbookList) {
            RespRollbookList respRollbook = RespRollbookList.of(rollbook);
            respRollbookList.add(respRollbook);
        }

        return respRollbookList;
    }

    // 연차현황에서 연차 개수 구하는 메소드
    @Override
    public AnnualCountResp selAnnualCount(String memberId) {
        Optional<AnnualLeave> optional = annualLeaveRepository.findAnnualLeaveByMemberId(memberId);
        if (optional.isEmpty())
            return null;
        return AnnualCountResp.of(optional.get());
    }

    // 연차 정보 수정
    @Override
    public Boolean udtAnnualCount(UdtAnnualReq req, String memberId) {
        try {
            Optional<AnnualLeave> optional = annualLeaveRepository.findAnnualLeaveByMemberId(memberId);
            if (optional.isEmpty())
                return false;

            AnnualLeave annualLeave = optional.get();
            annualLeave.setAnnualCount(req.getTotalAnnual());
            annualLeave.setAnnualLeft(req.getRemainedAnnual());

            return annualLeaveRepository.save(annualLeave) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 연차기록 현황
    // @Override
    // public Page<AnnualLeaveRecordResp> selLeaveRecordList(String memberId,
    // AnnualSearch search, Pageable pageable) {
    // Page<LeaveRecord> entityPage =
    // leaveRecordRepository.findLeaveRecordsByConditions(memberId, pageable);
    // return AnnualLeaveRecordResp.of(entityPage);
    // }

    // 연차기록 현황
    @Override
    public Page<AnnualLeaveRecordResp> selLeaveRecordList(Principal principal, AnnualSearch search, Pageable pageable) {
        Page<LeaveRecord> entityPage = rollbookRepository.selAnnualByConditions(pageable, search, principal);
        return AnnualLeaveRecordResp.of(entityPage);
    }

    // 연차 종류 코드 리스트
    @Override
    public List<RespAnnualCode> selAnnualCodeList(String codeType) {
        List<CodeInfo> codeInfoList = codeInfoRepository.findCodeInfoByCodeTypeAndCodeFlagFalse(codeType);
        return RespAnnualCode.of(codeInfoList);
    }

    /**
     * 23-9-2
     * 근태 페이징
     **/

    public Page<Rollbook> selRollbookEntity(Pageable pageable, RollbookSearch search, Principal principal) {
        Page<Rollbook> rollPage = rollbookRepository.selRollbookPaging(pageable, search, principal);
        return rollPage;
    }

    public Page<RollbookPageResp> sellRollbookPagingList(Pageable pageable, RollbookSearch search,
            Principal principal) {
        return RollbookPageResp.toDto(selRollbookEntity(pageable, search, principal));
    }

    /**
     * 23-9-3
     * 관리자 근태 페이징
     **/

    public Page<Rollbook> selAdminRollbookEntity(Pageable pageable, RollbookSearch search) {
        Page<Rollbook> rollPage = rollbookRepository.selAdminRollbookPaging(pageable, search);

        return rollPage;
    }

    public Page<RollbookPageResp> selAdminRollbookPagingList(Pageable pageable, RollbookSearch search) {
        return RollbookPageResp.toDto(selAdminRollbookEntity(pageable, search));
    }

    // 정정 요청된 근태 리스트
    @Override
    public List<CorrectRollbookResp> selAdminCorrectionRollbookList() {
        return CorrectRollbookResp.toDto(adminRequestRepository.findAdminRequestByConditions("근태정정요청"));
    }

    // 정정 요청 정보 저장
    @Override
    public Boolean insCorrentRollbookRequest(CorrectRollbookReq req) {
        List<AdminRequest> list = adminRequestRepository.saveAll(req.toEntity());
        return !list.isEmpty();
    }

    /**
     * 23-9-4 adminUpdateDto rollbookNo 찾기
     *
     **/
    public RespAdminRollbook selAdminEditRollbook(Long rollbookNo) {
        Rollbook rollbook = rollbookRepository.findById(rollbookNo).orElseThrow(EntityExistsException::new);

        return RespAdminRollbook.toDto(rollbook);
    }

    // 근태 정보 업데이트
    @Override
    public Boolean udtAdminEditRollbook(AdminRollbookUdtReq req) {
        try {
            Optional<Rollbook> optional = rollbookRepository.findById(req.getRollbookNo());
            if (optional.isEmpty())
                return false;

            Rollbook rollbook = optional.get();
            LocalDate date = rollbook.getRollbookOpenTime().toLocalDate();
            rollbook.setRollbookOpenState(req.getOpenState());
            rollbook.setRollbookCloseState(req.getCloseState());
            rollbook.setRollbookOpenTime(req.getOpenTime(date));
            rollbook.setRollbookCloseTime(req.getCloseTime(date));
            rollbook.setRollbookContents(req.getContents());

            if (rollbookRepository.save(rollbook) == null)
                return false;

            // 정정 요청 있으면 flag Y로 바꾸기
            List<AdminRequest> adminRequestList = adminRequestRepository
                    .findAdminRequestByRollbookNo(req.getRollbookNo());
            if (!adminRequestList.isEmpty()) {
                for (AdminRequest request : adminRequestList)
                    request.setRequestFlag("Y");
                adminRequestRepository.saveAll(adminRequestList);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * 근태 삭제
     **/
    public RollbookPageResp delRollbook(Long rollBookNo) {

        Rollbook rollbook = rollbookRepository.findById(rollBookNo).orElseThrow(EntityExistsException::new);

        rollbook.setRollbookFlag("N");

        List<AdminRequest> adminRequestList = adminRequestRepository.findAdminRequestByRollbookNo(rollBookNo);
        if (!adminRequestList.isEmpty()) {
            for (AdminRequest request : adminRequestList)
                request.setRequestFlag("Y");
            adminRequestRepository.saveAll(adminRequestList);
        }

        rollbook.setRollbookFlag("N");

        rollbookRepository.save(rollbook);

        return RollbookPageResp.toDto(rollbook);
    }

    // 근태 기준 시간 수정 요청
    @Override
    public Boolean udtRollbookSetting(RollbookSettingUdtReq req) {
        try {
            List<RollbookSetting> settings = rollbookSettingRepository.findAll();
            if (settings.isEmpty())
                return false;

            RollbookSetting setting = settings.get(0);
            setting.setSettingOpenTime(req.getOpenTime());
            setting.setSettingCloseTime(req.getCloseTime());

            return rollbookSettingRepository.save(setting) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 자정 근태 자동 처리 (새로운 근태 정보 생성 및 전날 근태 정보 미퇴근 처리)
    @Override
    public void insMidnightRollbook() {
        // 생성 & 수정된 Rollbook 리스트
        List<Rollbook> updatedRollbookList = new ArrayList<>();

        // 새로운 근태 리스트 생성
        LocalDate today = LocalDate.now();  // 오늘 날짜
        LocalDateTime todayMidnight = today.atStartOfDay(); // 오늘 자정
        List<MemberInfo> memberList = memberInfoRepository.findAllByMemberFlag("0");    // 퇴사자가 아닌 직원 리스트
        for(MemberInfo member : memberList) {
            Rollbook newRollbook = Rollbook.builder()
                    .rollbookOpenState("미출근")
                    .rollbookOpenTime(todayMidnight)
                    .rollbookFlag("Y")
                    .member(member)
                    .build();
            updatedRollbookList.add(newRollbook);
        }

        // 어제 미퇴근 처리
        List<Rollbook> yesterdayRollbookList = rollbookRepository.findAllByDay(todayMidnight, todayMidnight.minusDays(1));
        for(Rollbook rollbook : yesterdayRollbookList) {
            rollbook.setRollbookCloseTime(todayMidnight.minusSeconds(1));
            rollbook.setRollbookCloseState("미퇴근");
            updatedRollbookList.add(rollbook);
        }

        // 저장
        rollbookRepository.saveAll(updatedRollbookList);
    }


    // 연차 생성 관련 로직
    @Override
    public void resetAnnualSetting() {
        LocalDate today = LocalDate.now();  // 오늘 날짜
        AnnualSetting annualSetting = annualSettingRepository.findAll().get(0);

        // 연차 생성일
        LocalDate annualSettingResetDate = LocalDate.parse(annualSetting.getAnuSetResetDate(), DateTimeFormatter.ISO_DATE);

        // 연차 생성일에 저장한 월/일과 일치하는지 확인
        boolean isSameMonthAndDay = annualSettingResetDate.getMonth() == today.getMonth()
                && annualSettingResetDate.getDayOfMonth() == today.getDayOfMonth();


        // 연차 생성일이면 연차 정보 생성
        if(isSameMonthAndDay) {
            // 퇴사자가 아닌 임직원의 연차 정보
            List<AnnualLeave> annualLeaveList = annualLeaveRepository.findAnnualLeavesByMemberFlag();

            // 업데이트 할 연차 정보
            List<AnnualLeave> updatedAnnualLeaveList = new ArrayList<>();

            for(AnnualLeave annualLeave : annualLeaveList) {
                //연차 개수 계산
                MemberInfo member = annualLeave.getMember();
                LocalDate enableDate = member.getMemberEnableDate();   // 입사일
                System.out.println("no: " + member.getMemberNo() + ", 이름: " + member.getMemberNameKr() + ", 입사일: " + enableDate);
                int employmentYears = Period.between(enableDate, today).getYears() - 1; // 입사 후 지난 시간 - 1
                if(employmentYears < 0) employmentYears = 0;    // 입사 후 지난 시간이 0보다 작으면 0으로 리셋
                Long incrementCnt = annualSetting.getAnuSetIncrementCnt() * (employmentYears / annualSetting.getAnuSetIncrementYear()); // 가산 연차수
                Long annualCnt = annualSetting.getAnuSetDefaultCnt() + incrementCnt;    //기본 생성 연차수 + 가산 연차수
                if(annualCnt > annualSetting.getAnuSetMaxCnt()) //최대 생성 개수보다 많으면 최대 개수로 맞춤
                    annualCnt = annualSetting.getAnuSetMaxCnt();
                else if(annualCnt < annualSetting.getAnuSetDefaultCnt())    //기본 생성 개수보다 작으면 기본 개수로 맞춤
                    annualCnt = annualSetting.getAnuSetDefaultCnt();

                annualLeave.setAnnualCount((double) annualCnt);
                annualLeave.setAnnualLeft((double) annualCnt);
                annualLeave.setAnnualEndDate(LocalDate.of(today.getYear(), 12, 31));    // 올해 12월 31일
                updatedAnnualLeaveList.add(annualLeave);
            }

            annualLeaveRepository.saveAll(updatedAnnualLeaveList);
        }
    }

}
