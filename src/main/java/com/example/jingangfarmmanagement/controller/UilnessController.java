package com.example.jingangfarmmanagement.controller;


import com.example.jingangfarmmanagement.repository.entity.Uilness;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.UilnessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("uilness")
public class UilnessController extends BaseController<Uilness>{
    @Autowired
    private UilnessService uilnessService;

    @Override
    protected BaseService<Uilness> getService() {
        return uilnessService;
    }
}
