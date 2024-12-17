package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.repository.NewsRepository;
import com.example.jingangfarmmanagement.repository.entity.Category;
import com.example.jingangfarmmanagement.repository.entity.News;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.CategoryService;
import com.example.jingangfarmmanagement.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("news")
public class NewsController extends BaseController<News> {
    @Autowired
    private NewsService newsService;

    @Override
    protected BaseService<News> getService() {
        return newsService;
    }

}