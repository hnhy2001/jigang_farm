package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.Farm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

import java.util.List;

@Repository
public interface FarmRepository extends BaseRepository<Farm>, JpaSpecificationExecutorWithProjection<Farm> {
    <R> List<R> findBy(Class<R> var2);
    Farm findByName(String name);
}
