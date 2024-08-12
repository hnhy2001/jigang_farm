package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.repository.entity.Log;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

import java.util.List;

@Repository
public interface LogRepository extends BaseRepository<Log>, JpaSpecificationExecutorWithProjection<Log> {
}
