package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByUserName(String userName);

}
