package com.dflow.repository;

import com.dflow.entity.CodeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CodeInfoRepository extends JpaRepository<CodeInfo, Long> {
    //코드타입, 코드네임으로 하나 찾기
    Optional<CodeInfo> findCodeInfoByCodeTypeAndCodeName(String codeType, String codeName);

    //특정 코드타입이고 활성화된 코드 찾기
    List<CodeInfo> findCodeInfoByCodeTypeAndCodeFlagFalse(String codeType);

    //값 비교
    List<CodeInfo> findCodeNameByCodeType(String codeType);

    // 반려 / 취소
    List<CodeInfo> findByCodeTypeAndCodeNameIn(String codeType, List<String> codeName);

    // 나머지
    List<CodeInfo> findByCodeTypeAndCodeName(String codeType, String codeName);


}
