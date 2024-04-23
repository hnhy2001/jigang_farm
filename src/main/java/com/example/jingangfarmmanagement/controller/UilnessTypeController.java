package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.UilnessType;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.UilnessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("uilness-type")
public class UilnessTypeController extends BaseController<UilnessType>{
    @Autowired
    private UilnessTypeService uilnessTypeService;

    @Override
    protected BaseService<UilnessType> getService() {
        return uilnessTypeService;
    }
}
