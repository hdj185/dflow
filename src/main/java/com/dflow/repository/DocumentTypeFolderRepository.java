package com.dflow.repository;


import com.dflow.entity.DocumentTypeFolder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface DocumentTypeFolderRepository extends JpaRepository<DocumentTypeFolder, Long> {

    // 문서 계층 전체 조회
    @EntityGraph(attributePaths = "childrenFolder", type = EntityGraph.EntityGraphType.LOAD)
    List<DocumentTypeFolder> findByParentFolderIsNullAndTypeFolderFlag(String typeFolderFlag);

    // 문서 전체 조회
    List<DocumentTypeFolder> findAllByTypeFolderFlag(String typeFolderFlag);

    DocumentTypeFolder findByOrderValue(Integer orderValue);

    Integer countByDepthAndTypeFolderFlag(int depth, String flag);

    List<DocumentTypeFolder> findByDepthAndTypeFolderFlagOrderByDepthAsc(int depth, String flag);

    List<DocumentTypeFolder> findByOrderValueGreaterThanOrderByOrderValueAsc(Integer orderValue);


}
