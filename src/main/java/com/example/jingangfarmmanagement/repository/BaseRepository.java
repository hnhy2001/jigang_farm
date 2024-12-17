package com.example.jingangfarmmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaSpecificationExecutor<T>, PagingAndSortingRepository<T, String>, JpaRepository<T, String> {
    List<T> findAll();
    T findById(UUID id);
    List<T> findAllByStatus(int status);
}

