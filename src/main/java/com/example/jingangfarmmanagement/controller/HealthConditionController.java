package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.HealthCondition;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.HealthConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("health-condition")
public class HealthConditionController extends BaseController<HealthCondition>{
    @Autowired
    HealthConditionService healthConditionService;
    @Override
    protected BaseService<HealthCondition> getService() {
        return healthConditionService;
    }
}
