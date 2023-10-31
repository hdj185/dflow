package com.dflow.repository;

import com.dflow.entity.CodeInfo;
import com.dflow.entity.ReferenceFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferenceFileRepository extends JpaRepository<ReferenceFile, Long> {

    List<ReferenceFile> findAllByBoardNoAndFileFlag(Long boardNo, boolean fileFlag);
    List<ReferenceFile> findAllByDocAprvNoAndFileFlag(Long docAprvNo, boolean fileFlag);
}
