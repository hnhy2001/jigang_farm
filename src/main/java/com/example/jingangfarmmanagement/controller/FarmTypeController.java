package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.FarmType;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.FarmTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("farm-type")
public class FarmTypeController extends BaseController<FarmType>{
    @Autowired
    FarmTypeService farmTypeService;
    @Override
    protected BaseService<FarmType> getService() {
        return farmTypeService;
    }
}
