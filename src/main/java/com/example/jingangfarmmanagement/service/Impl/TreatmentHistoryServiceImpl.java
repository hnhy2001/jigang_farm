package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.mapper.TreatmentCardMapper;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.TreatmentHistoryReq;
import com.example.jingangfarmmanagement.model.response.MaterialRes;
import com.example.jingangfarmmanagement.model.response.TreatmentHistoryRes;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.service.TreatmentHistoryService;
import com.example.jingangfarmmanagement.uitl.Constant;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreatmentHistoryServiceImpl extends BaseServiceImpl<TreatmentHistory> implements TreatmentHistoryService {
    private static final String DELETED_FILTER =";status>-1" ;
    @Autowired
    TreatmentHistoryRepository treatmentHistoryRepository;
    @Autowired
    MaterialsRepository materialsRepository;
    @Autowired
    TreatmentHistoryMaterialRepository treatmentHistoryMaterialRepository;
    @Autowired
    HistoryHealthRepository historyHealthRepository;
    @Autowired
    TreatmentCardMapper treatmentCardMapper;
    @Override
    protected BaseRepository<TreatmentHistory> getRepository() {
        return treatmentHistoryRepository;
    }
    @Override
    public BaseResponse createTreatmentHistory(List<TreatmentHistoryReq> reqList) {
        try {
            for(var req:reqList) {
                TreatmentHistory treatmentHistory = new TreatmentHistory();
                treatmentHistory.setStatus(1);
                treatmentHistory.setTreatmentCardId(req.getTreatmentCardId());
                treatmentHistory.setCheckingDate(req.getCheckingDate());
                treatmentHistoryRepository.save(treatmentHistory);
                List<HistoryHealth> historyHealths = req.getHistoryHealths().stream().map(historyHealthReq -> {
                    HistoryHealth historyHealth = new HistoryHealth();
                    historyHealth.setUnit(historyHealthReq.getUnit());
                    historyHealth.setType(historyHealthReq.getType());
                    historyHealth.setStatus(1);
                    historyHealth.setResult(historyHealthReq.getResult());

                    historyHealth.setTreatmentHistory(treatmentHistory);
                    return historyHealth;
                }).collect(Collectors.toList());
                historyHealthRepository.saveAll(historyHealths);
                for (var materialReq : req.getMaterials()) {
                    Materials material = materialsRepository.findById(materialReq.getMaterialId())
                            .orElseThrow();
                    TreatmentHistoryMaterial treatmentCardMaterial = new TreatmentHistoryMaterial();
                    treatmentCardMaterial.setTreatmentHistoryId(treatmentHistory.getId());
                    treatmentCardMaterial.setMaterialId(material.getId());
                    treatmentCardMaterial.setQuantity(materialReq.getQuantity());
                    treatmentHistoryMaterialRepository.save(treatmentCardMaterial);
                }
              }
            return new BaseResponse(200, "OK", null);
        } catch (Exception e) {
            return new BaseResponse(500, "Internal Server Error", null);
        }
    }
    @Override
    @Transactional
    public BaseResponse updateTreatmentHistory(Long treatmentCardHistoryId, List<TreatmentHistoryReq> reqList) {
        TreatmentHistory treatmentHistory = treatmentHistoryRepository.findById(treatmentCardHistoryId)
                .orElseThrow(() -> new EntityNotFoundException("Treatment card not found"));
        // Update the properties
        for(var req:reqList) {
            treatmentHistory.setTreatmentCardId(req.getTreatmentCardId());
            treatmentHistory.setCheckingDate(req.getCheckingDate());
            treatmentHistoryRepository.save(treatmentHistory);
            List<HistoryHealth> exHistoryHealths= historyHealthRepository.findByTreatmentHistory(treatmentHistory);
            historyHealthRepository.deleteAll(exHistoryHealths);
            exHistoryHealths.clear();
            List<HistoryHealth> historyHealths = req.getHistoryHealths().stream().map(historyHealthReq -> {
                HistoryHealth historyHealth = new HistoryHealth();
                historyHealth.setUnit(historyHealthReq.getUnit());
                historyHealth.setType(historyHealthReq.getType());
                historyHealth.setStatus(1);
                historyHealth.setResult(historyHealthReq.getResult());
                historyHealth.setTreatmentHistory(treatmentHistory);
                return historyHealth;
            }).collect(Collectors.toList());
            historyHealthRepository.saveAll(historyHealths);
            // Update materials
            List<TreatmentHistoryMaterial> existingMaterials = treatmentHistoryMaterialRepository.findByTreatmentHistoryId(treatmentHistory.getId());
            treatmentHistoryMaterialRepository.deleteAll(existingMaterials);
            existingMaterials.clear();
            for (var materialReq : req.getMaterials()) {
                Materials material = materialsRepository.findById(materialReq.getMaterialId())
                        .orElseThrow(() -> new EntityNotFoundException(Constant.MATERIAL_NOT_FOUND));
                TreatmentHistoryMaterial treatmentCardMaterial = new TreatmentHistoryMaterial();
                treatmentCardMaterial.setTreatmentHistoryId(treatmentHistory.getId());
                treatmentCardMaterial.setMaterialId(material.getId());
                treatmentCardMaterial.setQuantity(materialReq.getQuantity());
                treatmentCardMaterial.setStatus(1);
                treatmentHistoryMaterialRepository.save(treatmentCardMaterial);
            }
        }
        return new BaseResponse(200, "OK", treatmentHistory);
    }
    @Override
    public BaseResponse getTreatmentHistoryById(Long id){
        TreatmentHistory treatmentHistory = treatmentHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Treatment History not found"));

        List<TreatmentHistoryMaterial> treatmentHistoryMaterials = treatmentHistoryMaterialRepository.findByTreatmentHistoryId(id);

        List<MaterialRes> materialList = treatmentHistoryMaterials.stream()
                .map(treatmentCardMaterial ->{
                    Materials material = materialsRepository.findById(treatmentCardMaterial.getMaterialId())
                            .orElseThrow(EntityNotFoundException::new);
                    return treatmentCardMapper.toMaterialRes(material, treatmentCardMaterial.getQuantity());
                })
                .collect(Collectors.toList());

        TreatmentHistoryRes treatmentHistoryRes = new TreatmentHistoryRes();
        treatmentHistoryRes.setTreatmentHistory(treatmentHistory);
        treatmentHistoryRes.setMaterial(materialList);
        return new BaseResponse(200, "OK", treatmentHistoryRes);
    }
    @Override
    public Page<TreatmentHistoryRes> searchTreatmentHistory(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<TreatmentHistory> spec = rootNode.accept(new CustomRsqlVisitor<>());
        Pageable pageable = getPage(req);
        Page<TreatmentHistory> treatmentHistories = treatmentHistoryRepository.findAll(spec, pageable);

        List<TreatmentHistoryRes> historyResList = treatmentHistories.getContent().stream()
                .map(treatmentHistory -> {
                    List<TreatmentHistoryMaterial> treatmentCardMaterials = treatmentHistoryMaterialRepository.findByTreatmentHistoryId(treatmentHistory.getId());
                    List<MaterialRes> materialList = treatmentCardMaterials.stream()
                            .map(treatmentCardMaterial ->{
                                Materials material = materialsRepository.findById(treatmentCardMaterial.getMaterialId())
                                        .orElseThrow(EntityNotFoundException::new);
                                return treatmentCardMapper.toMaterialRes(material, treatmentCardMaterial.getQuantity());
                            })
                            .collect(Collectors.toList());

                    TreatmentHistoryRes treatmentHistoryRes = new TreatmentHistoryRes();
                    treatmentHistoryRes.setTreatmentHistory(treatmentHistory);
                    treatmentHistoryRes.setMaterial(materialList);
                    return treatmentHistoryRes;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(historyResList, pageable, treatmentHistories.getTotalElements());
    }
    protected Pageable getPage(SearchReq req) {
        String[] sortList = req.getSort().split(",");
        Sort.Direction direction = Sort.Direction.DESC;
        String sortBy = "id";

        if (sortList.length > 1) {
            direction = sortList[1].equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            sortBy = sortList[0];
        }

        return req.getSize() != null ?
                PageRequest.of(req.getPage(), req.getSize(), direction, sortBy) :
                Pageable.unpaged();
    }
}
