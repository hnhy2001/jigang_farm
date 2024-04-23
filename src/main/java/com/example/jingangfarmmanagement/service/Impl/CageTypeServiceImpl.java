package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.CageTypeRepository;
import com.example.jingangfarmmanagement.repository.entity.CageType;
import com.example.jingangfarmmanagement.service.CageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CageTypeServiceImpl extends BaseServiceImpl<CageType> implements CageTypeService {
    @Autowired
    CageTypeRepository cageTypeRepository;
    @Override
    protected BaseRepository<CageType> getRepository() {
        return cageTypeRepository;
    }
}
