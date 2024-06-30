package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.CageNoteHistoryRepository;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.CageNoteHistory;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.CageNoteHistoryService;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CageNoteHistoryServiceImpl extends BaseServiceImpl<CageNoteHistory> implements CageNoteHistoryService {
    @Autowired
    CageNoteHistoryRepository cageNoteHistoryRepository;
    @Override
    protected BaseRepository<CageNoteHistory> getRepository() {
        return cageNoteHistoryRepository;
    }
    @Override
    public BaseResponse createCageNoteHistory(CageNoteHistory cageNoteHistory)  {

        Optional<CageNoteHistory> cageNoteHistoryExist =cageNoteHistoryRepository.findByDate(cageNoteHistory.getCreateDate());
        if (cageNoteHistoryExist.isPresent()) {
           cageNoteHistoryExist.get().setCage(cageNoteHistory.getCage());
           cageNoteHistoryExist.get().setNote(cageNoteHistory.getNote());
           cageNoteHistoryExist.get().setWarning(cageNoteHistory.getWarning());
           cageNoteHistoryRepository.save(cageNoteHistoryExist.get());
        }
        else {
            CageNoteHistory cageNoteHistoryNew =new CageNoteHistory();
            cageNoteHistoryNew.setCage(cageNoteHistory.getCage());
            cageNoteHistoryNew.setNote(cageNoteHistory.getNote());
            cageNoteHistoryNew.setWarning(cageNoteHistory.getWarning());
            cageNoteHistoryRepository.save(cageNoteHistoryNew);
        }
        return new BaseResponse(200, "Thêm mới vật nuôi thành công", null);
    }

}
