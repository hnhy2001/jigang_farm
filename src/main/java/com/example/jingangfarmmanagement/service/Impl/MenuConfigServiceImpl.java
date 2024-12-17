package com.example.jingangfarmmanagement.service.Impl;


import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.MenuConfigRepository;
import com.example.jingangfarmmanagement.repository.NewsRepository;
import com.example.jingangfarmmanagement.repository.entity.MenuConfig;
import com.example.jingangfarmmanagement.repository.entity.News;
import com.example.jingangfarmmanagement.service.MenuConfigService;
import com.example.jingangfarmmanagement.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuConfigServiceImpl extends BaseServiceImpl<MenuConfig> implements MenuConfigService {
    @Autowired
    private MenuConfigRepository menuConfigRepository;

    @Override
    protected BaseRepository<MenuConfig> getRepository() {
        return menuConfigRepository;
    }




}

