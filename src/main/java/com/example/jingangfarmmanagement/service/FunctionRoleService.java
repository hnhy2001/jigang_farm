package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.repository.entity.FunctionRole;
import com.example.jingangfarmmanagement.repository.entity.Role;

import java.util.List;

public interface FunctionRoleService extends BaseService<FunctionRole> {
    List<FunctionRole> getByRole(Role role);
}
