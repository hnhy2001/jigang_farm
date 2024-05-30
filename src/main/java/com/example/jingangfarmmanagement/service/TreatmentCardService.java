package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.TreatmentCardReq;
import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import org.springframework.data.domain.Page;

import java.util.List;


public interface TreatmentCardService extends BaseService<TreatmentCard> {
    public BaseResponse createTreatment(TreatmentCardReq req);
    public BaseResponse updateTreatment(Long treatmentCardId,TreatmentCardReq req);
    public Page<TreatmentCard> findTreatmentHistoriesByPet(List<Long> petIds, int page, int size);
}
