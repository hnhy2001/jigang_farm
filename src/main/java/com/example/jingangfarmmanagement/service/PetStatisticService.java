package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ChangeCageReq;
import com.example.jingangfarmmanagement.model.req.ChangeStatusPetReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.UpdateWeightPetReq;
import com.example.jingangfarmmanagement.projection.PetProjection;
import com.example.jingangfarmmanagement.repository.dto.PetStatisticDto;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface PetStatisticService extends BaseService<Pet> {

    public PetStatisticDto petStatistic(Long startDate, Long endDate, List<Integer> sex, List<Integer> status, List<Long> cageId, List<Long> farmId, int age) ;
    public List<Object[]> filterPetDeath(Long startDate, Long endDate, List<Integer> sex, List<Integer> status, List<Long> cageId, List<Long> farmId, int age) ;

}
