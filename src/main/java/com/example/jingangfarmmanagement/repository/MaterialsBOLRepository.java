package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.BOL;
import com.example.jingangfarmmanagement.repository.entity.MaterialsBOL;

import java.util.List;

public interface MaterialsBOLRepository extends BaseRepository<MaterialsBOL>{
    List<MaterialsBOL> findAllByBol(BOL bol);
}
