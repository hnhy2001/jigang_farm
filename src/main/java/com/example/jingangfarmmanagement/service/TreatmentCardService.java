package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.TreatmentCardReq;
import com.example.jingangfarmmanagement.model.response.TreatmentCardRes;
import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


public interface TreatmentCardService extends BaseService<TreatmentCard> {
    public BaseResponse createTreatment(TreatmentCardReq req);
    public BaseResponse updateTreatment(Long treatmentCardId,TreatmentCardReq req);
    public BaseResponse getTreatmentCardById(Long id);
    public Page<TreatmentCardRes> searchTreatmentCard(SearchReq req);
}
