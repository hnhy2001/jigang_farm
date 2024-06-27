package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.repository.entity.Uilness;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UilnessRepository extends BaseRepository<Uilness>{
    Uilness findByName(String name);
}
