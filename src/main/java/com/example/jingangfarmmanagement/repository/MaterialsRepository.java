package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.Materials;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialsRepository extends BaseRepository<Materials>{
    Optional<Materials> findByNameAndCargo(String name, String cargo);
}
