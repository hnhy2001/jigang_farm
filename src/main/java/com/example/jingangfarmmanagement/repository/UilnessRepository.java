package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.repository.entity.Uilness;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UilnessRepository extends BaseRepository<Uilness>{
    Optional<Uilness> findByName(String name);
}
