package com.example.jingangfarmmanagement.service.Impl;


import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.CategoryRepository;
import com.example.jingangfarmmanagement.repository.NewsRepository;
import com.example.jingangfarmmanagement.repository.entity.Category;
import com.example.jingangfarmmanagement.repository.entity.News;
import com.example.jingangfarmmanagement.service.CategoryService;
import com.example.jingangfarmmanagement.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl extends BaseServiceImpl<News> implements NewsService {
    @Autowired
    private NewsRepository newsRepository;

    @Override
    protected BaseRepository<News> getRepository() {
        return newsRepository;
    }




}

