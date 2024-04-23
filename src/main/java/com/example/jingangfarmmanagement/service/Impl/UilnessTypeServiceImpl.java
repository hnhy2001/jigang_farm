package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.UilnessTypeRepository;
import com.example.jingangfarmmanagement.repository.entity.UilnessType;
import com.example.jingangfarmmanagement.service.UilnessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UilnessTypeServiceImpl extends BaseServiceImpl<UilnessType> implements UilnessTypeService {
    @Autowired
    private UilnessTypeRepository uilnessTypeRepository;
    @Override
    protected BaseRepository<UilnessType> getRepository() {
        return uilnessTypeRepository;
    }
}
