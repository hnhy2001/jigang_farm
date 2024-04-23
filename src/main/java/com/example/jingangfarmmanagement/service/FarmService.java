package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.projection.FarmProjection;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import org.springframework.data.domain.Page;

public interface FarmService extends BaseService<Farm> {
    Page<FarmProjection> customSearch(SearchReq req);
}
