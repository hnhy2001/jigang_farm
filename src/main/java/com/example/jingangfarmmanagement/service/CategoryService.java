package com.example.jingangfarmmanagement.service;


import com.example.jingangfarmmanagement.repository.entity.Category;
import com.example.jingangfarmmanagement.repository.entity.Role;

import java.util.List;

public interface CategoryService extends BaseService<Category> {
    public List<Category> getCategoryTree();
    public Category getCategoryTreeById(String parentId);
}
