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

}
