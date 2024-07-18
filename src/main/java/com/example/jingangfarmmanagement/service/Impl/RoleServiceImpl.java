package com.example.jingangfarmmanagement.service.Impl;


import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.req.AssignRoleMenuReq;
import com.example.jingangfarmmanagement.repository.entity.Function;
import com.example.jingangfarmmanagement.repository.entity.FunctionRole;
import com.example.jingangfarmmanagement.repository.entity.Role;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.RoleRepository;
import com.example.jingangfarmmanagement.service.FunctionRoleService;
import com.example.jingangfarmmanagement.service.FunctionService;
import com.example.jingangfarmmanagement.service.RoleService;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FunctionRoleService functionRoleService;

    @Autowired
    private FunctionService functionService;

    @Override
    protected BaseRepository<Role> getRepository() {
        return roleRepository;
    }

    @Override
    public List<Role> getAllByInId(List<Long> idList) {
        return roleRepository.findByIdInAndStatus(idList, Status.ACTIVE);
    }

    @Override
    public Role create(Role t) throws Exception {
        t.setStatus(Status.ACTIVE);
        t.setCreateDate(DateUtil.getCurrenDateTime());
        Role role = roleRepository.save(t);
        List<Function> functionList = functionService.getAll();
        List<FunctionRole> functionRoleList = new ArrayList<>();
        functionList.stream().forEach(e -> {
            FunctionRole functionRole = new FunctionRole();
            functionRole.setRole(role);
            functionRole.setFunction(e);
            functionRole.setStatus(1);
            functionRoleList.add(functionRole);
        });
        functionRoleService.createAll(functionRoleList);
        return role;
    }
}

