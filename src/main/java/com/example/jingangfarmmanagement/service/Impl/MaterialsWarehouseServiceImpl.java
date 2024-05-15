package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.MaterialsWarehouseRepository;
import com.example.jingangfarmmanagement.repository.entity.MaterialsWarehouse;
import com.example.jingangfarmmanagement.service.MaterialsWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialsWarehouseServiceImpl extends BaseServiceImpl<MaterialsWarehouse> implements MaterialsWarehouseService {
    @Autowired
    MaterialsWarehouseRepository materialsWarehouseRepository;
    @Override
    protected BaseRepository<MaterialsWarehouse> getRepository() {
        return materialsWarehouseRepository;
    }
}
