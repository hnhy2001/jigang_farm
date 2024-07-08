package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.entity.CageNoteHistory;
import com.example.jingangfarmmanagement.repository.entity.CageType;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.CageNoteHistoryService;
import com.example.jingangfarmmanagement.service.CageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("cage-note")
public class CageNoteHistoryController extends BaseController<CageNoteHistory> {
    @Autowired
    CageNoteHistoryService cageNoteHistoryService;
    @Override
    protected BaseService<CageNoteHistory> getService() {
        return cageNoteHistoryService;
    }
    @PostMapping("/create-cage-note")
    public BaseResponse create(@RequestBody CageNoteHistory cageNoteHistory) throws Exception {
        return cageNoteHistoryService.createCageNoteHistory(cageNoteHistory);
    }
    @PutMapping("/change-reaction-cage-note")
    public BaseResponse changeCageNoteReaction(@RequestParam Long cageNoteId, int isReaction) throws  Exception {
        return  cageNoteHistoryService.changeCageNoteReaction(cageNoteId,isReaction);
    }
}
