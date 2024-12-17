package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.MaterialsWarehouse;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.MaterialsWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("materials_warehouse")
public class MaterialsWarehouseController extends BaseController<MaterialsWarehouse>{
    @Autowired
    MaterialsWarehouseService materialsWarehouseService;
    @Override
    protected BaseService<MaterialsWarehouse> getService() {
        return materialsWarehouseService;
    }
}
