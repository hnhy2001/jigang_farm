package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.Materials;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialsRepository extends BaseRepository<Materials>{
    Materials findByName(String name);
}
