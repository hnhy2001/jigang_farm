package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.Cage;
import org.springframework.stereotype.Repository;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

import java.util.Optional;

@Repository
public interface CageRepository extends BaseRepository<Cage>, JpaSpecificationExecutorWithProjection<Cage> {
   Cage findByCode(String code);
}
