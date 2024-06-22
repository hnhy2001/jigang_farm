package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.ChangeCageHistoryRepository;
import com.example.jingangfarmmanagement.repository.PetRepository;
import com.example.jingangfarmmanagement.repository.entity.ChangeCageHistory;
import com.example.jingangfarmmanagement.service.ChangeCageHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangeCageHistoryImpl extends BaseServiceImpl<ChangeCageHistory> implements ChangeCageHistoryService {
    private static final String DELETED_FILTER =";status>-1" ;

    @Autowired
    PetRepository petRepository;
    @Autowired
    ChangeCageHistoryRepository changeCageHistoryRepository;
    @Override
    protected BaseRepository<ChangeCageHistory> getRepository() {
        return changeCageHistoryRepository;
    }

}
