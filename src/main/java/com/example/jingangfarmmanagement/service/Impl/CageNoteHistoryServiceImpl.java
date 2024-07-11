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

import javax.swing.text.html.Option;
import java.util.*;
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

        Optional<CageNoteHistory> cageNoteHistoryExist =cageNoteHistoryRepository.findByDate(cageNoteHistory.getCreateDate(), cageNoteHistory.getCage().getId());
        if (cageNoteHistoryExist.isPresent()) {
           cageNoteHistoryExist.get().setCage(cageNoteHistory.getCage());
           cageNoteHistoryExist.get().setNote(cageNoteHistory.getNote());
           cageNoteHistoryExist.get().setWarning(cageNoteHistory.getWarning());
           cageNoteHistoryExist.get().setIsReaction(cageNoteHistory.getIsReaction());
           cageNoteHistoryRepository.save(cageNoteHistoryExist.get());
           return new BaseResponse(200, "Cập nhật ghi chú thành công", cageNoteHistoryExist.get());
        }
        else {
            CageNoteHistory cageNoteHistoryNew =new CageNoteHistory();
            cageNoteHistoryNew.setCage(cageNoteHistory.getCage());
            cageNoteHistoryNew.setNote(cageNoteHistory.getNote());
            cageNoteHistoryNew.setWarning(cageNoteHistory.getWarning());
            cageNoteHistoryNew.setCreateDate(cageNoteHistory.getCreateDate());
            cageNoteHistoryNew.setStatus(1);
            cageNoteHistoryNew.setIsReaction(0);
            cageNoteHistoryRepository.save(cageNoteHistoryNew);
            return new BaseResponse(200, "Thêm mới ghi chú thành công", cageNoteHistoryNew);
        }
    }

    @Override
    public BaseResponse changeCageNoteReaction(Long cageNoteId, int isReaction) {
        CageNoteHistory cageNoteHistoryResult = cageNoteHistoryRepository.findAllById(cageNoteId);
        cageNoteHistoryResult.setIsReaction(isReaction);
        cageNoteHistoryRepository.save(cageNoteHistoryResult);
        return new BaseResponse(200, "Success", null);
    }

}
