package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.UilnessRepository;
import com.example.jingangfarmmanagement.repository.entity.Uilness;
import com.example.jingangfarmmanagement.service.UilnessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UilnessServiceImpl extends BaseServiceImpl<Uilness> implements UilnessService {
    @Autowired
    UilnessRepository uilnessRepository;
    @Override
    protected BaseRepository<Uilness> getRepository() {
        return uilnessRepository;
    }
}
