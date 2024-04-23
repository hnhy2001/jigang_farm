package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.HistoryRepository;
import com.example.jingangfarmmanagement.repository.entity.History;
import com.example.jingangfarmmanagement.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl extends BaseServiceImpl<History> implements HistoryService {
    @Autowired
    HistoryRepository historyRepository;

    @Override
    protected BaseRepository<History> getRepository() {
        return historyRepository;
    }
}
