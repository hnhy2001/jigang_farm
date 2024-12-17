package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.projection.FarmProjection;
import com.example.jingangfarmmanagement.projection.StatisticFarmProjection;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FarmService extends BaseService<Farm> {
    public Page<Farm> customSearch(SearchReq req,Long userId);
    List<Farm> findByFilter();
}
