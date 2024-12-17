package com.example.jingangfarmmanagement.service.Impl;


import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.CategoryRepository;
import com.example.jingangfarmmanagement.repository.RoleRepository;
import com.example.jingangfarmmanagement.repository.entity.Category;
import com.example.jingangfarmmanagement.repository.entity.Role;
import com.example.jingangfarmmanagement.service.CategoryService;
import com.example.jingangfarmmanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    protected BaseRepository<Category> getRepository() {
        return categoryRepository;
    }
    @Override
    public List<Category> getCategoryTree() {
        List<Category> categories = categoryRepository.findAll();
        Map<String, Category> categoryMap = categories.stream()
                .collect(Collectors.toMap(c -> c.getId().toString(), c -> c));

        List<Category> tree = new ArrayList<>();
        for (Category category : categories) {
            if (category.getParentId() == null) {
                tree.add(category);
            } else {
                Category parent = categoryMap.get(category.getParentId());
                if (parent != null) {
                    parent.getChildren().add(category);
                }
            }
        }
        return tree;
    }
    @Override
    public Category getCategoryTreeById(String parentId) {
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + parentId));

        List<Category> allCategories = categoryRepository.findAll();

        buildCategoryTree(parentCategory, allCategories);

        return parentCategory;
    }

    private void buildCategoryTree(Category parent, List<Category> allCategories) {
        List<Category> children = allCategories.stream()
                .filter(c -> parent.getId().equals(c.getParentId()))
                .collect(Collectors.toList());

        parent.setChildren(children);

        // Đệ quy để tiếp tục tìm con của từng danh mục con
        for (Category child : children) {
            buildCategoryTree(child, allCategories);
        }
    }

}

