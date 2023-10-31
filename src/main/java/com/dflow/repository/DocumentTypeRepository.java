package com.dflow.repository;

import com.dflow.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
    //flag에 따른 양식 리스트 모두 가져오기
    List<DocumentType> findByDocFormUseFlagOrderByOrderValue(String docFormUseFlag);

    @Modifying
    @Query("UPDATE DocumentType SET orderValue = :orderValue WHERE docFormTypeNo = :docFormTypeNo")
    int updateOrderValueByDocFormTypeNo(@Param("orderValue") Integer orderValue, @Param("docFormTypeNo") Long docFormTypeNo);

    Integer countByDocFormUseFlag(String flag);


}
