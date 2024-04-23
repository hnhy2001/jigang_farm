package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.projection.CageProjection;
import com.example.jingangfarmmanagement.projection.FarmProjection;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import org.springframework.data.domain.Page;

public interface CageService extends BaseService<Cage>{
    Page<CageProjection> customSearch(SearchReq req);
}
