package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.ExportPet;
import com.example.jingangfarmmanagement.repository.entity.ExportPetStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportPetRepository extends BaseRepository<ExportPet> {
//    SELECT
//    c.name ,
//    p.sex as sex,
//    COUNT(p.sex) AS sexQuantity
//    FROM pet p
//    JOIN export_pet ep ON p.id = ep.pet_id
//    JOIN cage c ON p.cage = c.id
//    WHERE c.id = :cageId
//    GROUP BY p.sex;
    @Query("SELECT c.name as cageName," +
            "p.sex as sex, " +
            "count(p.sex) as sexQuantity from Pet p " +
            "join ExportPet ep ON p.id=ep.petId " +
            "join Cage c ON p.cage.id= c.id " +
            "where c.id=:cageId " +
            "GROUP BY p.sex")
    List<ExportPetStatistic> statisticExportPetWithCage(Long cageId);
    @Query(value = "select ep from ExportPet ep " +
            "join Pet p on ep.petId = p.id " +
            "join Cage c on p.cage.id = c.id " +
            "where (:cageId is null or c.id = :cageId)" +
            "and (:name is null or p.name like %:name%) " +
            "and (:code is null or p.code like %:code%) " +
            "and (:sex is null or p.sex = :sex) " +
            "and (:age is null or p.age = :age) " +
            "and (:type is null or ep.type = :type) " +
            "and (:startExportDate is null or ep.exportDate >= :startExportDate) " +
            "and (:endExportDate is null or ep.exportDate <= :endExportDate) " +
            "and (:note is null or ep.note like %:note%)",
            countQuery = "select count(ep) from ExportPet ep " +
                    "join Pet p on ep.petId = p.id " +
                    "join Cage c on p.cage.id = c.id " +
                    "where (:cageId is null or c.id = :cageId)" +
                    "and (:name is null or p.name like %:name%) " +
                    "and (:code is null or p.code like %:code%) " +
                    "and (:sex is null or p.sex = :sex) " +
                    "and (:age is null or p.age = :age) " +
                    "and (:type is null or ep.type = :type) " +
                    "and (:startExportDate is null or ep.exportDate >= :startExportDate) " +
                    "and (:endExportDate is null or ep.exportDate <= :endExportDate) " +
                    "and (:note is null or ep.note like %:note%)")
    Page<ExportPet> searchExportPet(
            @Param("cageId") Long cageId,
            @Param("name") String name,
            @Param("code") String code,
            @Param("sex") Integer sex,
            @Param("age") Integer age,
            @Param("type") Integer type,
            @Param("startExportDate") Long startExportDate,
            @Param("endExportDate") Long endExportDate,
            @Param("note") String note,
            Pageable pageable
    );
}
