package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.Pet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

import java.util.List;

@Repository
public interface PetRepository extends BaseRepository<Pet>, JpaSpecificationExecutorWithProjection<Pet> {
    List<Pet> findByIdIn(List<Long> ids);

    List<Pet> findAllByStatus(int status);

    boolean existsByName(String name);
    Pet findByName(String name);
    @Query("select p from Pet p join Cage c on p.cage.id=c.id " +
            "join Farm f on c.farm.id = f.id " +
            "where p.name=:name " +
            "AND c.name=:cageName " +
            "AND f.name=:farmName ")
    Pet findByCageAndFarmAndName( String name,String cageName, String farmName);
}
