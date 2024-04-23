package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.FarmRepository;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.service.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FarmServiceImpl extends BaseServiceImpl<Farm> implements FarmService {
    @Autowired
    private FarmRepository farmReponsitory;

    @Override
    protected BaseRepository<Farm> getRepository() {
        return farmReponsitory;
    }
}
