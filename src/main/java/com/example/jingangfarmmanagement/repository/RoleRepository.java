package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends BaseRepository<Role> {

    List<Role> findByIdInAndStatus(List<Long> idList, Integer status);
}
