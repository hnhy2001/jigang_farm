package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.MaterialsWarehouseRepository;
import com.example.jingangfarmmanagement.repository.entity.Materials;
import com.example.jingangfarmmanagement.repository.entity.MaterialsWarehouse;
import com.example.jingangfarmmanagement.service.MaterialsWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialsWarehouseServiceImpl extends BaseServiceImpl<MaterialsWarehouse> implements MaterialsWarehouseService {
    @Autowired
    MaterialsWarehouseRepository materialsWarehouseRepository;
    @Override
    protected BaseRepository<MaterialsWarehouse> getRepository() {
        return materialsWarehouseRepository;
    }

    @Override
    public List<MaterialsWarehouse> createMaterialsWarehouses(List<MaterialsWarehouse> materialsWarehouses) {
        return materialsWarehouseRepository.saveAll(materialsWarehouses);
    }

    @Override
    public List<MaterialsWarehouse> getByMaterials(Materials materials) {
        return List.of();
    }
}
