package com.example.jingangfarmmanagement.service.Impl;


import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.repository.entity.Role;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.RoleRepository;
import com.example.jingangfarmmanagement.service.RoleService;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

