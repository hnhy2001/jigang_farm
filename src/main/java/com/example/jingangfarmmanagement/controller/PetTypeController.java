package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.PetType;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("pet-type")
public class PetTypeController extends BaseController<PetType>{
    @Autowired
    PetTypeService petTypeService;
    @Override
    protected BaseService<PetType> getService() {
        return petTypeService;
    }
}
