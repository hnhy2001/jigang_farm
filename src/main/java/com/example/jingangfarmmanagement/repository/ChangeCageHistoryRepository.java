package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.ChangeCageHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeCageHistoryRepository extends BaseRepository<ChangeCageHistory> {
    @Query(value = "SELECT c FROM ChangeCageHistory c " +
            "WHERE (:cageNameFrom IS NULL OR c.cageNameFrom LIKE %:cageNameFrom%) " +
            "AND (:cageNameTo IS NULL OR c.cageNameTo LIKE %:cageNameTo%) " +
            "AND (:farmNameFrom IS NULL OR c.farmNameFrom LIKE %:farmNameFrom%) " +
            "AND (:farmNameTo IS NULL OR c.farmNameTo LIKE %:farmNameTo%) " +
            "AND (:minDate IS NULL OR :maxDate IS NULL OR c.createDate BETWEEN :minDate AND :maxDate) " +
            "AND (:petName IS NULL OR c.pet.name LIKE %:petName%) " +
            "AND c.status >=1 " +
            "GROUP BY c.createDate",
            countQuery = "SELECT COUNT(c) FROM ChangeCageHistory c " +
                    "WHERE (:cageNameFrom IS NULL OR c.cageNameFrom LIKE %:cageNameFrom%) " +
                    "AND (:cageNameTo IS NULL OR c.cageNameTo LIKE %:cageNameTo%) " +
                    "AND (:farmNameFrom IS NULL OR c.farmNameFrom LIKE %:farmNameFrom%) " +
                    "AND (:farmNameTo IS NULL OR c.farmNameTo LIKE %:farmNameTo%) " +
                    "AND (:minDate IS NULL OR :maxDate IS NULL OR c.createDate BETWEEN :minDate AND :maxDate) " +
                    "AND (:petName IS NULL OR c.pet.name LIKE %:petName%) " +
                    "AND c.status >=1 " +
                    "GROUP BY c.createDate")
    Page<ChangeCageHistory> searchChangeCageHistory(@Param("cageNameFrom") String cageNameFrom,
                                                    @Param("cageNameTo") String cageNameTo,
                                                    @Param("farmNameFrom") String farmNameFrom,
                                                    @Param("farmNameTo") String farmNameTo,
                                                    @Param("minDate") Long minDate,
                                                    @Param("maxDate") Long maxDate,
                                                    @Param("petName") String petName,
                                                    Pageable pageable);


}
