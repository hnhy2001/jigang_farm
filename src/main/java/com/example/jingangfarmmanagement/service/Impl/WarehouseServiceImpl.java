package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.WarehouseRepository;
import com.example.jingangfarmmanagement.repository.entity.Warehouse;
import com.example.jingangfarmmanagement.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl extends BaseServiceImpl<Warehouse> implements WarehouseService {
    @Autowired
    WarehouseRepository warehouseRepository;
    @Override
    protected BaseRepository<Warehouse> getRepository() {
        return warehouseRepository;
    }
}
