package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.HealthConditionRepository;
import com.example.jingangfarmmanagement.repository.entity.HealthCondition;
import com.example.jingangfarmmanagement.service.HealthConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthConditionServiceImpl extends BaseServiceImpl<HealthCondition> implements HealthConditionService {
    @Autowired
    HealthConditionRepository healthConditionRepository;
    @Override
    protected BaseRepository<HealthCondition> getRepository() {
        return healthConditionRepository;
    }
}
