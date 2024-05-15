package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.Warehouse;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("warehouse")
public class WarehouseController extends BaseController<Warehouse>{
    @Autowired
    WarehouseService warehouseService;
    @Override
    protected BaseService<Warehouse> getService() {
        return warehouseService;
    }
}
