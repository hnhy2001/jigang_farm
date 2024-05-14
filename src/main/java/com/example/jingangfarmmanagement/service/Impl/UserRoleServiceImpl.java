package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.UserRoleRepository;
import com.example.jingangfarmmanagement.repository.entity.User;
import com.example.jingangfarmmanagement.repository.entity.UserRole;
import com.example.jingangfarmmanagement.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements UserRoleService {
    @Autowired
    UserRoleRepository userRoleRepository;
    @Override
    protected BaseRepository<UserRole> getRepository() {
        return userRoleRepository;
    }

    @Override
    public List<UserRole> getUserRoleByUserId(User user) {
        return userRoleRepository.findAllByUser(user);
    }
}
