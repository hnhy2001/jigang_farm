package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.CageRepository;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.service.CageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CageServiceImpl extends BaseServiceImpl<Cage> implements CageService {
    @Autowired
    private CageRepository cageReponsitory;

    @Override
    protected BaseRepository<Cage> getRepository() {
        return cageReponsitory;
    }
}
