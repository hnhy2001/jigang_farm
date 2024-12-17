package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.CageType;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.CageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("cage-type")
public class CageTypeController extends BaseController<CageType>{
    @Autowired
    CageTypeService cageTypeService;
    @Override
    protected BaseService<CageType> getService() {
        return cageTypeService;
    }
}
