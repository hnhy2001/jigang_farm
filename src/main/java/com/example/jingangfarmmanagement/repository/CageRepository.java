package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import org.springframework.stereotype.Repository;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface CageRepository extends BaseRepository<Cage>, JpaSpecificationExecutorWithProjection<Cage> {
   Cage findByCode(String code);
   List<Cage> findAllByFarmAndStatus(Farm farm, int status);
   Cage findByName(String name);
}
