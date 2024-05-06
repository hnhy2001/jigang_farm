package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.Materials;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.MaterialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("materials")
public class MaterialsController extends BaseController<Materials>{
    @Autowired
    MaterialsService materialsService;

    @Override
    protected BaseService<Materials> getService() {
        return materialsService;
    }
}
