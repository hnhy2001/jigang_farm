package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.Pet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

import java.util.List;

@Repository
public interface PetRepository extends BaseRepository<Pet>, JpaSpecificationExecutorWithProjection<Pet> {
    List<Pet> findByIdIn(List<Long> ids);
    @Query("Select p from Pet p where p.id >=9230")
    List<Pet> findByIdNon();
    List<Pet> findAllByStatus(int status);

    boolean existsByName(String name);
    Pet findByName(String name);
    @Query("select p from Pet p join Cage c on p.cage.id=c.id " +
            "join Farm f on c.farm.id = f.id " +
            "where p.name=:name " +
            "AND c.name=:cageName " +
            "AND f.name=:farmName " +
            "AND p.status !=-1 ")
    Pet findByCageAndFarmAndName( String name,String cageName, String farmName);
    @Query("select p from Pet p join p.cage c join c.farm f " +
            "where (:cageId is null or c.id = (:cageId )) " +
            "AND p.status !=-1 " +
            "and (:startDate is null or p.createDate >= :startDate ) " +
            "and (:endDate is null or p.createDate <= :endDate )")
    List<Pet> findByCageId(Long cageId, Long startDate, Long endDate);
    @Query("select p from Pet p join p.cage c join c.farm f " +
            "where f.id = :farmId " +
            "and (:startDate is null or p.createDate >= :startDate) " +
            "and (:endDate is null or p.createDate <= :endDate)" +
            "AND p.status !=-1 ")
    List<Pet> findByFarmId(  Long farmId, Long startDate, Long endDate);

    @Query(value = "SELECT * FROM pet " +
            "WHERE id = (SELECT MAX(id) FROM pet WHERE SUBSTRING(name, 1, 4) = :month) " +
            "AND CAST(RIGHT(name, 1) AS UNSIGNED) % 2 = 1", nativeQuery = true)
    Pet findMenPetByNunbersOfMonth(String month);

    @Query(value = "SELECT * FROM pet " +
            "WHERE id = (SELECT MAX(id) FROM pet WHERE SUBSTRING(name, 1, 4) = :month) " +
            "AND CAST(RIGHT(name, 1) AS UNSIGNED) % 2 = 0", nativeQuery = true)
    Pet findWomenPetByNunbersOfMonth(String month);



}
