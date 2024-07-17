package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("farm")
@PreAuthorize("@appAuthorizer.authorize(authentication, 'VIEW', this)")
public class FarmController extends BaseController<Farm> {
    @Autowired
    FarmService farmService;

    @Override
    protected BaseService<Farm> getService() {
        return farmService;
    }
}
