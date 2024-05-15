package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.FunctionRole;
import com.example.jingangfarmmanagement.repository.entity.HealthCondition;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.FunctionRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("function_role")
public class FunctionRoleController extends BaseController<FunctionRole>{
    @Autowired
    FunctionRoleService functionRoleService;
    @Override
    protected BaseService<FunctionRole> getService() {
        return functionRoleService;
    }
}
