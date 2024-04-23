package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.PetTypeRepository;
import com.example.jingangfarmmanagement.repository.entity.PetType;
import com.example.jingangfarmmanagement.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetTypeServiceImpl extends BaseServiceImpl<PetType> implements PetTypeService {
    @Autowired
    PetTypeRepository petTypeRepository;
    @Override
    protected BaseRepository<PetType> getRepository() {
        return petTypeRepository;
    }
}
