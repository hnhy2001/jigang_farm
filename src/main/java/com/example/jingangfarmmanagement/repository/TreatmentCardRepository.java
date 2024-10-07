package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreatmentCardRepository extends BaseRepository<TreatmentCard> {
    @Query(value = "SELECT tc.* " +
            "FROM treatment_card tc JOIN treatment_card_pet tcp ON tc.id = tcp.treatment_card_id " +
            "JOIN pet p ON p.id = tcp.pet_id " +
            "WHERE p.id IN :petIds",
            countQuery = "SELECT COUNT(*) " +
                    "FROM treatment_card tc JOIN treatment_card_pet tcp ON tc.id = tcp.treatment_card_id " +
                    "JOIN pet p ON p.id = tcp.pet_id " +
                    "WHERE p.id IN :petIds",
            nativeQuery = true)
    Page<TreatmentCard> findTreatmentCardsByPet(@Param("petIds") List<Long> petIds, Pageable pageable);

//    @Query(value = "SELECT t FROM TreatmentCard t " +
//            "JOIN t.uilnesses u " +
//            "JOIN t.pets p " +
//            "WHERE (:code IS NULL OR t.code like %:code%) " +
//            "AND (:status IS NULL OR t.status = :status) " +
//            "AND (:createBy IS NULL OR t.createdBy LIKE %:createBy%) " +
//            "AND (:uilnessName IS NULL OR u.name LIKE %:uilnessName%) " +
//            "AND (:petName IS NULL OR p.name LIKE %:petName%) " +
//            "AND ((:startDate IS NULL AND :endDate IS NULL) OR t.createDate BETWEEN :startDate AND :endDate)"
//    )
//    Page<TreatmentCard> searchTreatmentCards(
//            @Param("code") String code,
//            @Param("status") Integer status,
//            @Param("createBy") String createBy,
//            @Param("uilnessName") String uilnessName,
//            @Param("petName") String petName,
//            @Param("startDate") Long startDate,
//            @Param("endDate") Long endDate,
//            Pageable pageable
//    );
@Query(value = "SELECT DISTINCT treatmentc0_.* \n" +
        "FROM treatment_card treatmentc0_ \n" +
        "INNER JOIN treatment_card_uiless uilnesses1_ ON treatmentc0_.id = uilnesses1_.treatment_card_id \n" +
        "INNER JOIN uilness uilness2_ ON uilnesses1_.uilness_id = uilness2_.id \n" +
        "INNER JOIN treatment_card_pet pets3_ ON treatmentc0_.id = pets3_.treatment_card_id \n" +
        "INNER JOIN pet pet4_ ON pets3_.pet_id = pet4_.id\n" +
        "WHERE (:code IS NULL OR treatmentc0_.code LIKE CONCAT('%', :code, '%')) \n" +
        "AND (:createBy IS NULL OR treatmentc0_.created_by LIKE CONCAT('%', :createBy, '%'))\n" +
        "AND (:name IS NULL OR uilness2_.name LIKE CONCAT('%', :name, '%')) \n" +
        "AND (:status IS NULL OR treatmentc0_.status = :status)\n" +
        "AND (:petname IS NULL OR pet4_.name LIKE CONCAT('%', :petname, '%')) \n" +
        "AND ((:startDate IS NULL AND :endDate IS NULL) \n" +
        "OR treatmentc0_.created_date BETWEEN :startDate AND :endDate) " +
        "ORDER BY treatmentc0_.id DESC",
        countQuery = "SELECT COUNT(DISTINCT treatmentc0_.*) \n" +
                "FROM treatment_card treatmentc0_ \n" +
                "INNER JOIN treatment_card_uiless uilnesses1_ ON treatmentc0_.id = uilnesses1_.treatment_card_id \n" +
                "INNER JOIN uilness uilness2_ ON uilnesses1_.uilness_id = uilness2_.id \n" +
                "INNER JOIN treatment_card_pet pets3_ ON treatmentc0_.id = pets3_.treatment_card_id \n" +
                "INNER JOIN pet pet4_ ON pets3_.pet_id = pet4_.id\n" +
                "WHERE (:code IS NULL OR treatmentc0_.code LIKE CONCAT('%', :code, '%')) \n" +
                "AND (:createBy IS NULL OR treatmentc0_.created_by LIKE CONCAT('%', :createBy, '%'))\n" +
                "AND (:name IS NULL OR uilness2_.name LIKE CONCAT('%', :name, '%')) \n" +
                "AND (:status IS NULL OR treatmentc0_.status = :status)\n" +
                "AND (:petname IS NULL OR pet4_.name LIKE CONCAT('%', :petname, '%')) \n" +
                "AND ((:startDate IS NULL AND :endDate IS NULL) \n" +
                "OR treatmentc0_.created_date BETWEEN :startDate AND :endDate)",
        nativeQuery = true)
Page<TreatmentCard> searchTreatmentCards(
        @Param("code") String code,
        @Param("status") Integer status,
        @Param("createBy") String createBy,
        @Param("name") String name,
        @Param("petname") String petName,
        @Param("startDate") Long startDate,
        @Param("endDate") Long endDate,
        Pageable pageable
);



}
