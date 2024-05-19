package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.MaterialsBOL;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.MaterialsBOLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("materials_bol")
public class MaterialsBOLController extends BaseController<MaterialsBOL> {
    @Autowired
    MaterialsBOLService materialsBOLService;

    @Override
    protected BaseService<MaterialsBOL> getService() {
        return materialsBOLService;
    }
}
