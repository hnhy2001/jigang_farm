package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.User;
import com.example.jingangfarmmanagement.repository.entity.UserRole;

import java.util.List;

public interface UserRoleRepository extends BaseRepository<UserRole>{
    List<UserRole> findAllByUser(User user);


}
