package com.dflow.dashboard.service.serviceImpl;

import com.dflow.dashboard.requestDto.ReqMainRollbook;
import com.dflow.dashboard.responseDto.*;
import com.dflow.dashboard.service.DashboardService;
import com.dflow.entity.*;
import com.dflow.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private final MemberInfoRepository memberInfoRepository;
    private final BoardRepository boardRepository;
    private final ProjectResourceRepository projectResourceRepository;
    private final AnnualLeaveRepository annualLeaveRepository;
    private final RollbookSettingRepository rollbookSettingRepository;
    private final RollbookRepository rollbookRepository;
    private final DocumentApprovalRepository documentApprovalRepository;
    private final ReferenceRepository referenceRepository;


    //공지사항 리스트 출력
    @Override
    public List<RespMainBoard> selNoticeMain() {
        //5개 제한
        Pageable pageable = PageRequest.of(0, 6);
        List<Board> entityList = boardRepository.findBoardsWithNoticeInMainNotice(pageable);
        List<RespMainBoard> list = RespMainBoard.of(entityList);

        //현재 공지로 등록된 게 5개 미만일 때
        if(list.size() < 6) {
            pageable = PageRequest.of(0, 6 - list.size());
            List<Board> boardList = boardRepository.findBoardsInMainNotice(pageable);
            List<RespMainBoard> responseList = RespMainBoard.ofGeneralNotice(boardList);
            list.addAll(responseList);
        }

        return list;
    }

    //신규게시글 리스트 출력
    @Override
    public List<RespMainBoard> selBoardMain() {
        Pageable pageable = PageRequest.of(0, 6);
        List<Board> entityList = boardRepository.findBoardsInMain(pageable);
        return RespMainBoard.of(entityList);
    }

    //프로젝트 리스트 출력
    @Override
    public List<RespMainProject> selProjectMain(String memberId) {
        List<ProjectResource> projectResources = projectResourceRepository.findProjectInfosByConditionsInMain(memberId);
        return RespMainProject.of(projectResources);
    }

    //결재 정보 출력
    @Override
    public RespMainAprv selAprvMain(String memberId) {
        // 현재 날짜에서 30일 전 날짜 계산
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        return new RespMainAprv(
                documentApprovalRepository.selMainApprovalCounts(memberId, "진행중"),
                documentApprovalRepository.selMainApprovalCounts(memberId, "완료"),
                documentApprovalRepository.selMainApprovalCounts(memberId, "반려"),
                documentApprovalRepository.selMainApprovalCounts(memberId, "대기"),
                referenceRepository.countReferencesByMember(memberId, thirtyDaysAgo));
    }

    //연차 정보 출력
    @Override
    public RespMainAnnual selAnnualMain(String memberId) {
        Optional<AnnualLeave> optional = annualLeaveRepository.findAnnualLeaveByMemberId(memberId);
        if(optional.isEmpty())
            return RespMainAnnual.builder()
                    .totalAnnual("0")
                    .usedAnnual("0")
                    .remainedAnnual("0")
                    .build();
        else
            return RespMainAnnual.of(optional.get());
    }

    //출근 정보 저장
    //무사히 저장했으면 true 반환, 실패하거나 오류 발생 시 false 반환
    @Override
    public boolean insRollbookMain(String memberId, ReqMainRollbook request) {
        try {
            List<Rollbook> rollbookList = rollbookRepository.findAllByMemberId(memberId, "Y");
            //근태기록이 없을 시 신규 생성
            if(rollbookList.isEmpty()) {
                MemberInfo member = memberInfoRepository.findByMemberId(memberId).get();
                rollbookRepository.save(request.toEntity(member));
                return true;
            }

            Rollbook lastRollbook = rollbookList.get(0);

            //마지막 기록이 같은 날 기록인지 판단하는 플래그
            boolean isTodayRollbook = lastRollbook.getRollbookOpenTime().toLocalDate()
                    .isEqual(request.getRollbookTime().toLocalDate());

            //마지막 기록 중에 퇴근 정보가 있다면 새로 생성
            if(!isTodayRollbook || lastRollbook.getRollbookCloseTime() != null) {
                MemberInfo member = memberInfoRepository.findByMemberId(memberId).get();
                rollbookRepository.save(request.toEntity(member));
            } else {
                //마지막 기록 중 퇴근 정보가 없다면 기존 기록 수정
                lastRollbook.setRollbookOpenState(request.getRollbookState());
                lastRollbook.setRollbookOpenTime(request.getRollbookTime());
                lastRollbook.setRollbookContents(request.getRollbookContents());
                rollbookRepository.save(lastRollbook);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //퇴근 정보 저장
    @Override
    public boolean udtRollbookMain(String memberId, ReqMainRollbook request) {
        List<Rollbook> rollbookList = rollbookRepository.findAllByMemberId(memberId, "Y");
        Rollbook lastestRollbook = rollbookList.get(0);
        try {
            rollbookRepository.save(request.toEntity(lastestRollbook));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //나의 출퇴근 정보 불러오기
    @Override
    public RespMainRollbook selRollbookMain(String memberId) {
        List<Rollbook> rollbookList = rollbookRepository.findAllByMemberId(memberId, "Y");
        if(rollbookList.isEmpty())  return new RespMainRollbook();  //근태 정보가 비어있을 때

        //근태 정보가 있을 시 가장 마지막 근태 정보 가져오기
        Rollbook lastRollbook = rollbookList.get(0);
        LocalDate lastOpenDate = lastRollbook.getRollbookOpenTime().toLocalDate();  //기존 기록 마지막 출근 날짜
        String lastState = lastRollbook.getRollbookCloseState();   //기존 기록 마지막 퇴근 상태 기록

        LocalDate currentDate = LocalDate.now();

        // 기존 기록 중 마지막 출근 날짜가 오늘과 동일한 경우
        if(lastOpenDate.isEqual(currentDate)) {
            return RespMainRollbook.of(lastRollbook);
        } else {
            List<Rollbook> updatedRollbooks = new ArrayList<>();

            //미출근 처리 근태 기록 생성
            Rollbook missedRollbook = Rollbook.builder()
                    .rollbookOpenState("미출근")
                    .rollbookOpenTime(currentDate.atStartOfDay())
                    .rollbookFlag("Y")
                    .member(lastRollbook.getMember())
                    .build();
            updatedRollbooks.add(missedRollbook);

            rollbookRepository.saveAll(updatedRollbooks);

            return RespMainRollbook.of(missedRollbook);
        }
    }

    //출퇴근 기준시간 불러오기
    @Override
    public RespMainStandard selStandardTimeMain() {
        List<RollbookSetting> settings = rollbookSettingRepository.findAll();
        return RespMainStandard.of(settings.get(0));
    }

    @Override
    public RespMainMemberInfo selMemberInfoMain(String memberId) {
        MemberInfo memberInfo = memberInfoRepository.findByMemberId(memberId).get();
        RespMainMemberInfo response = RespMainMemberInfo.of(memberInfo);

        return response;
    }
}
