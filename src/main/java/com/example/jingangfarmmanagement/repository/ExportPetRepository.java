package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.ExportPet;
import com.example.jingangfarmmanagement.repository.entity.ExportPetStatistic;
import org.springframework.data.jpa.repository.Query;
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
}
