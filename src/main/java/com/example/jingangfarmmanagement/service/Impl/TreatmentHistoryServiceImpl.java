package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.TreatmentHistoryRepository;
import com.example.jingangfarmmanagement.repository.entity.TreatmentHistory;
import com.example.jingangfarmmanagement.service.TreatmentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreatmentHistoryServiceImpl extends BaseServiceImpl<TreatmentHistory> implements TreatmentHistoryService {
    @Autowired
    TreatmentHistoryRepository treatmentHistoryRepository;
    @Override
    protected BaseRepository<TreatmentHistory> getRepository() {
        return treatmentHistoryRepository;
    }
}
