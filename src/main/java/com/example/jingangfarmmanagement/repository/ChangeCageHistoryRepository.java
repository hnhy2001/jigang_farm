package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.ChangeCageHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeCageHistoryRepository extends BaseRepository<ChangeCageHistory> {
    @Query(value = "SELECT c.create_date as createDate, " +
            "GROUP_CONCAT(c.id, '|', c.cage_Name_From, '|', c.cage_Name_To, '|', c.farm_Name_From, '|', c.farm_Name_To, '|', c.status, '|', c.pet_Id SEPARATOR ';') as changeCageHistories " +
            "FROM Change_Cage_History c " +
            "WHERE (:cageNameFrom IS NULL OR c.cage_Name_From LIKE %:cageNameFrom%) " +
            "AND (:cageNameTo IS NULL OR c.cage_Name_To LIKE %:cageNameTo%) " +
            "AND (:farmNameFrom IS NULL OR c.farm_Name_From LIKE %:farmNameFrom%) " +
            "AND (:farmNameTo IS NULL OR c.farm_Name_To LIKE %:farmNameTo%) " +
            "AND (:minDate IS NULL OR :maxDate IS NULL OR c.create_Date BETWEEN :minDate AND :maxDate) " +
            "AND (:petName IS NULL OR (SELECT p.name FROM Pet p WHERE p.id = c.pet_Id) LIKE %:petName%) " +
            "AND c.status >= 1 " +
            "GROUP BY c.create_Date",
            nativeQuery = true)
    List<Object[]> searchChangeCageHistoryNative(@Param("cageNameFrom") String cageNameFrom,
                                                 @Param("cageNameTo") String cageNameTo,
                                                 @Param("farmNameFrom") String farmNameFrom,
                                                 @Param("farmNameTo") String farmNameTo,
                                                 @Param("minDate") Long minDate,
                                                 @Param("maxDate") Long maxDate,
                                                 @Param("petName") String petName);



}
