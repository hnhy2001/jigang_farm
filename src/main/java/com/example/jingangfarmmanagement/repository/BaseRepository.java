package com.example.jingangfarmmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaSpecificationExecutor<T>, PagingAndSortingRepository<T, Long>, JpaRepository<T, Long> {
    List<T> findAll();
    T findAllById(Long id);
}
