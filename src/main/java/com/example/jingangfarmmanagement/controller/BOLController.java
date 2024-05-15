package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.BOL;
import com.example.jingangfarmmanagement.service.BOLService;
import com.example.jingangfarmmanagement.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("bol")
public class BOLController extends BaseController<BOL>{
    @Autowired
    private BOLService bolService;
    @Override
    protected BaseService<BOL> getService() {
        return bolService;
    }
}
