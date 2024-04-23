package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.Farm;
import org.springframework.stereotype.Repository;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

@Repository
public interface FarmRepository extends BaseRepository<Farm>, JpaSpecificationExecutorWithProjection<Farm> {

}
