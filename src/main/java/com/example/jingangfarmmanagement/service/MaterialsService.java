package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.repository.entity.Materials;

import java.util.List;

public interface MaterialsService extends BaseService<Materials> {
    List<Materials> createMaterials(List<Materials> materials);
}
