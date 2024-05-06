package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ChangeCageReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.projection.FarmProjection;
import com.example.jingangfarmmanagement.projection.PetProjection;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import org.springframework.data.domain.Page;

public interface PetService extends BaseService<Pet> {
    public BaseResponse changeCage(ChangeCageReq changCageReq);
    Page<PetProjection> customSearch(SearchReq req);
    PetProjection customDetails(Long id);

}
