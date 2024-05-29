package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.TreatmentHistoryReq;
import com.example.jingangfarmmanagement.model.response.TreatmentHistoryRes;
import com.example.jingangfarmmanagement.repository.entity.TreatmentHistory;
import org.springframework.data.domain.Page;

public interface TreatmentHistoryService extends BaseService<TreatmentHistory>{
    public BaseResponse createTreatmentHistory(TreatmentHistoryReq req);
    public BaseResponse updateTreatmentHistory(Long treatmentCardHistoryId,TreatmentHistoryReq req);
    public BaseResponse getTreatmentHistoryById(Long id);
    public Page<TreatmentHistoryRes> searchTreatmentHistory(SearchReq req);
}
