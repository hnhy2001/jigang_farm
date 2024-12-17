package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.projection.CageProjection;
import com.example.jingangfarmmanagement.projection.FarmProjection;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CageService extends BaseService<Cage>{
    public Page<Cage> customSearch(SearchReq req,Long userId);
    BaseResponse quantityPet();
    List<Cage> getByFarm(Farm farm);
}
