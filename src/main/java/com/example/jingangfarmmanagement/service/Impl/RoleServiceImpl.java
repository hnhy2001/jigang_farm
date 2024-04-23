package com.example.jingangfarmmanagement.service.Impl;


import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.req.AssignRoleMenuReq;
import com.example.jingangfarmmanagement.repository.entity.Role;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.RoleRepository;
import com.example.jingangfarmmanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected BaseRepository<Role> getRepository() {
        return roleRepository;
    }

    @Override
    public List<Role> getAllByInId(List<Long> idList) {
        return roleRepository.findByIdInAndStatus(idList, Status.ACTIVE);
    }
}

