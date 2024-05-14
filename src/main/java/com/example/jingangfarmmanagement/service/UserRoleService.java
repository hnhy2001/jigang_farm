package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.repository.entity.User;
import com.example.jingangfarmmanagement.repository.entity.UserRole;

import java.util.List;

public interface UserRoleService extends BaseService<UserRole>{
    List<UserRole> getUserRoleByUserId(User user);
}
