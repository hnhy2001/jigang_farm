package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.repository.entity.MaterialsWarehouse;

import java.util.List;

public interface MaterialsWarehouseService extends BaseService<MaterialsWarehouse> {
    List<MaterialsWarehouse> createMaterialsWarehouses(List<MaterialsWarehouse> materialsWarehouses);
}
