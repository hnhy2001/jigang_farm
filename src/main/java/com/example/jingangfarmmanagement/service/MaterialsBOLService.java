package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.repository.entity.BOL;
import com.example.jingangfarmmanagement.repository.entity.MaterialsBOL;

import java.util.List;

public interface MaterialsBOLService extends BaseService<MaterialsBOL> {
    List<MaterialsBOL> createMaterialsBOLList(List<MaterialsBOL> materialsBOLList);
    List<MaterialsBOL> getByBOL(BOL bol);
}
