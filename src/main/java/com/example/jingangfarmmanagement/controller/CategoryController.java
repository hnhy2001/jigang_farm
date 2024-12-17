package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.AssignRoleMenuReq;
import com.example.jingangfarmmanagement.repository.entity.Category;
import com.example.jingangfarmmanagement.repository.entity.Role;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.CategoryService;
import com.example.jingangfarmmanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("categories")
public class CategoryController extends BaseController<Category> {
    @Autowired
    private CategoryService categoryService;

    @Override
    protected BaseService<Category> getService() {
        return categoryService;
    }

    @GetMapping("/tree")
    public List<Category> getCategoryTree() {
        return categoryService.getCategoryTree();
    }
    @GetMapping("/tree/by-id")
    public Category getCategoryTreeById(@RequestParam String parentId){
        return  categoryService.getCategoryTreeById(parentId);
    }
}