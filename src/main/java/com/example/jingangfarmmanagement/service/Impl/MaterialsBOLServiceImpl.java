package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.MaterialsBOLRepository;
import com.example.jingangfarmmanagement.repository.entity.MaterialsBOL;
import com.example.jingangfarmmanagement.service.MaterialsBOLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialsBOLServiceImpl extends BaseServiceImpl<MaterialsBOL> implements MaterialsBOLService {
    @Autowired
    private MaterialsBOLRepository materialsBOLRepository;
    @Override
    protected BaseRepository<MaterialsBOL> getRepository() {
        return materialsBOLRepository;
    }

    @Override
    public List<MaterialsBOL> createMaterialsBOLList(List<MaterialsBOL> materialsBOLList) {
        return materialsBOLRepository.saveAll(materialsBOLList);
    }
}
