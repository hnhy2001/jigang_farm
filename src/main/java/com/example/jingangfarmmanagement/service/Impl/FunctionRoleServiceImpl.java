package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.FunctionRoleRepository;
import com.example.jingangfarmmanagement.repository.entity.FunctionRole;
import com.example.jingangfarmmanagement.repository.entity.Role;
import com.example.jingangfarmmanagement.service.FunctionRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunctionRoleServiceImpl extends BaseServiceImpl<FunctionRole> implements FunctionRoleService {
    @Autowired
    private FunctionRoleRepository functionRoleRepository;
    @Override
    protected BaseRepository<FunctionRole> getRepository() {
        return functionRoleRepository;
    }

    @Override
    public List<FunctionRole> getByRole(Role role) {
        return functionRoleRepository.findAllByRole(role);
    }
}
