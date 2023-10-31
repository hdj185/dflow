package com.dflow.board.service.impl;

import com.dflow.attachFile.requestDto.ReqFile;
import com.dflow.board.requestDto.BrdNoticePageReq;
import com.dflow.board.requestDto.BrdNoticeUploadPageReq;
import com.dflow.board.requestDto.BrdUdtPageReq;
import com.dflow.board.requestDto.ReferenceFileDto;
import com.dflow.board.responseDto.AttachFileInfo;
import com.dflow.board.responseDto.BrdNoticeInfoResp;
import com.dflow.board.responseDto.BrdNoticePageResp;
import com.dflow.board.service.BoardService;
import com.dflow.entity.*;
import com.dflow.project.responseDto.RespDetailProject;
import com.dflow.repository.AttachFileRepository;
import com.dflow.repository.BoardRepository;
import com.dflow.repository.MemberInfoRepository;
import com.dflow.repository.ReferenceFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final AttachFileRepository attachFileRepository;
    private final ReferenceFileRepository referenceFileRepository;
    private final BrdNoticeInfoResp.BrdNoticeInfoMapper brdNoticeInfoMapper;

    @Value("${file.uploadDir}")
    private String uploadPath;

    @Override
    public List<BrdNoticeInfoResp> selNoticeList() {
        List<Board> entityList = boardRepository.findBoardsWithNotice();
        return BrdNoticeInfoResp.of(entityList);
    }

    @Override
    public BrdNoticePageResp<BrdNoticeInfoResp> noticePageList(BrdNoticePageReq brdNoticePageReq) {

        String[] types = brdNoticePageReq.getTypes();
        String keyword = brdNoticePageReq.getKeyword();
        Pageable pageable = brdNoticePageReq.getPageable("boardNo");

        log.info("service 정보 keyword : "+ keyword);

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable, "Y");


        List<BrdNoticeInfoResp> brdNoticeInfoRespList = result.getContent().stream()
                .map(board -> brdNoticeInfoMapper.apply(board)).collect(Collectors.toList());

        return BrdNoticePageResp.<BrdNoticeInfoResp>withAll()
                .brdNoticePageReq(brdNoticePageReq)
                .pageResponseList(brdNoticeInfoRespList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public boolean insBoardNotice(BrdNoticeUploadPageReq pageReq, MultipartFile[] uploadFile, String memberName) throws IOException {

        try {
            // NoSuchElementException 대비
            MemberInfo member = memberInfoRepository.findByMemberId(memberName)
                    .orElseThrow(() -> new NoSuchElementException("멤버 정보를 찾을 수 없습니다: " + memberName));
            // 게시글 내용 저장
            pageReq.setBoardFlag(false);
            Long boardNo = boardRepository.save(pageReq.toEntity(member)).getBoardNo();

            if (uploadFile != null && uploadFile.length > 0) {  // 파일이 있을 경우

                for (MultipartFile file : uploadFile) {
                    // 파일이 비어있을 경우 건너뛴다.
                    if (file.isEmpty()) {
                        continue;
                    }

                    ReqFile reqFile = new ReqFile(file, uploadPath);

                    // 실제 파일 저장
                    file.transferTo(new File(reqFile.getFileLocation()));

                    // 데이터베이스에 파일 정보 저장
                    AttachFile attachFile = reqFile.toEntity();
                    ReferenceFileDto fileDto = new ReferenceFileDto(boardNo, attachFileRepository.save(attachFile).getFileAttachNo(), member);
                    referenceFileRepository.save(fileDto.toEntitiy());
                }

            }

            // 처리 성공 결과 반환
            return true;

        } catch (IOException ioe) {
            // 파일 입출력 에러 처리
            ioe.printStackTrace();
            return false;
        } catch (Exception e) {
            // 다른 예외 처리
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean udtBoard(BrdUdtPageReq pageReq, MultipartFile[] uploadFile, String memberName, List<Long> delFiles) throws IOException {
        try {
            // NoSuchElementException 대비
            MemberInfo member = memberInfoRepository.findByMemberId(memberName)
                    .orElseThrow(() -> new NoSuchElementException("멤버 정보를 찾을 수 없습니다: " + memberName));
            log.info("수정자 정보 : "+ member.getMemberNo());
            // 게시글 내용 업데이트
            Board board = boardRepository.findById(pageReq.getBoardNo()).orElseThrow();
            Long boardNo = boardRepository.save(pageReq.udtEntity(board, member)).getBoardNo();

            if (uploadFile != null && uploadFile.length > 0) {  // 파일이 있을 경우

                for (MultipartFile file : uploadFile) {
                    // 파일이 비어있을 경우 건너뛴다.
                    if (file.isEmpty()) {
                        continue;
                    }

                    ReqFile reqFile = new ReqFile(file, uploadPath);

                    // 실제 파일 저장
                    file.transferTo(new File(reqFile.getFileLocation()));

                    // 데이터베이스에 파일 정보 저장
                    AttachFile attachFile = reqFile.toEntity();
                    ReferenceFileDto fileDto = new ReferenceFileDto(boardNo, attachFileRepository.save(attachFile).getFileAttachNo(), member);
                    referenceFileRepository.save(fileDto.toEntitiy());
                }

            }
            // 참조 파일 flag 변경
            if(delFiles.size() > 0){
                for(Long no : delFiles){
                    ReferenceFile referenceFile = referenceFileRepository.findById(no).orElseThrow();
                    referenceFile.changeFileFlag(true);
                    referenceFileRepository.save(referenceFile);
                }
            }

            // 처리 성공 결과 반환
            return true;

        } catch (IOException ioe) {
            // 파일 입출력 에러 처리
            ioe.printStackTrace();
            return false;
        } catch (Exception e) {
            // 다른 예외 처리
            e.printStackTrace();
            return false;
        }
    }

    // 게시글 상세 조회

    @Override
    public BrdNoticeInfoResp selDetailBoard(Long boardNo) {

        Board board = boardRepository.findById(boardNo).get();

        if (board.getBoardNotice() == null || board.getBoardNotice().equals("N")) {
            return brdNoticeInfoMapper.apply(board);
        } else {
            return BrdNoticeInfoResp.of(board);
        }
    }

    @Override
    public BrdNoticePageResp<BrdNoticeInfoResp> boardPageList(BrdNoticePageReq brdNoticePageReq) {

        String[] types = brdNoticePageReq.getTypes();
        String keyword = brdNoticePageReq.getKeyword();
        Pageable pageable = brdNoticePageReq.getPageable("boardNo");

        log.info("service 정보 keyword : "+ keyword);

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable, "N");


        List<BrdNoticeInfoResp> brdNoticeInfoRespList = result.getContent().stream()
                .map(board -> brdNoticeInfoMapper.apply(board)).collect(Collectors.toList());

        return BrdNoticePageResp.<BrdNoticeInfoResp>withAll()
                .brdNoticePageReq(brdNoticePageReq)
                .pageResponseList(brdNoticeInfoRespList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public List<AttachFileInfo> selAttachFileList(Long boardNo) {

        List<AttachFileInfo> fileList = referenceFileRepository.findAllByBoardNoAndFileFlag(boardNo, false)
                .stream()
                .map(AttachFileInfo::toDto)
                .collect(Collectors.toList());

        return fileList;
    }

    @Override
    public Boolean delBoard(Long boardNo) {
        try {
            Optional<Board> optional = boardRepository.findById(boardNo);
            if(optional.isPresent()) {
                Board board = optional.get();
                board.setBoardFlag(true);
                return boardRepository.save(board).getBoardFlag();
            }
            return false;
        } catch(Exception e) {
            return false;
        }
    }
}
