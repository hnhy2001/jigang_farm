package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.CageNoteHistory;
import org.springframework.stereotype.Service;

@Service
public interface CageNoteHistoryService extends BaseService<CageNoteHistory> {
    public BaseResponse createCageNoteHistory(CageNoteHistory cageNoteHistory);
}
