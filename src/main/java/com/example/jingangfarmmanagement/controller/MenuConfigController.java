package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.entity.Category;
import com.example.jingangfarmmanagement.repository.entity.MenuConfig;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.CategoryService;
import com.example.jingangfarmmanagement.service.MenuConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("menu_config")
public class MenuConfigController extends BaseController<MenuConfig> {
    @Autowired
    private MenuConfigService menuConfigService;

    @Override
    protected BaseService<MenuConfig> getService() {
        return menuConfigService;
    }

}