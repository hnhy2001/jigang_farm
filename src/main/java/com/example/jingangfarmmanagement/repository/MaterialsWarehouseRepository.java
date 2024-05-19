package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.MaterialsWarehouse;

import java.util.List;

public interface MaterialsWarehouseRepository extends BaseRepository<MaterialsWarehouse>{
    List<MaterialsWarehouse> findAllByMaterials(MaterialsWarehouse materialsWarehouse);
}
