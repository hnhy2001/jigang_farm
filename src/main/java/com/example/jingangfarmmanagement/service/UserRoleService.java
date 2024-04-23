package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.repository.entity.Role;
import com.example.jingangfarmmanagement.repository.entity.UserRole;

import java.util.List;

public interface UserRoleService extends BaseService<UserRole> {
    List<UserRole> getAllByUserId(Long id);

    void deleteByUser(Long userId);

    List<Role> getRolesByUserId(Long userId);
}
