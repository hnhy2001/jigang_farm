package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.ChangeCageHistory;
import org.springframework.data.domain.Pageable;

public interface ChangeCageHistoryService extends BaseService<ChangeCageHistory> {
    public BaseResponse searchChangeCageHistoryCustom(String cageNameFrom, String cageNameTo,
                                                      String farmNameFrom, String farmNameTo,
                                                      Long minDate, Long maxDate,
                                                      String petName, Pageable pageable);
}
