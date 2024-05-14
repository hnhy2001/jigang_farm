package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.FunctionRole;
import com.example.jingangfarmmanagement.repository.entity.Role;

import java.util.List;

public interface FunctionRoleRepository extends BaseRepository<FunctionRole>{
    List<FunctionRole> findAllByRole(Role role);
}
