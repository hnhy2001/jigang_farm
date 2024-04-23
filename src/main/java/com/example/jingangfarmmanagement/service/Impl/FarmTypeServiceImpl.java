package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.FarmTypeRepository;
import com.example.jingangfarmmanagement.repository.entity.FarmType;
import com.example.jingangfarmmanagement.service.FarmTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FarmTypeServiceImpl extends BaseServiceImpl<FarmType> implements FarmTypeService {
    @Autowired
    FarmTypeRepository farmTypeRepository;

    @Override
    protected BaseRepository<FarmType> getRepository() {
        return farmTypeRepository;
    }
}
