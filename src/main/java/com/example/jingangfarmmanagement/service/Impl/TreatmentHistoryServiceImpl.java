package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.mapper.TreatmentCardMapper;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.TreatmentCardMaterialReq;
import com.example.jingangfarmmanagement.model.req.TreatmentHistoryReq;
import com.example.jingangfarmmanagement.model.response.MaterialRes;
import com.example.jingangfarmmanagement.model.response.TreatmentHistoryRes;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.service.TreatmentHistoryService;
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
    public BaseResponse createTreatmentHistory(List<TreatmentHistoryReq> reqList,Long quantityPet) {
        try {
            for(var req:reqList) {
                TreatmentHistory treatmentHistory = new TreatmentHistory();
                treatmentHistory.setStatus(1);
                treatmentHistory.setTreatmentCardId(req.getTreatmentCardId());
                treatmentHistory.setCheckingDate(req.getCheckingDate());
                treatmentHistory.setUpdatedBy(req.getUpdatedBy());
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
//                    if(!calculatorMaterial(material,materialReq,null,true,quantityPet)){
//                        return new BaseResponse(500, "Số lượng trong kho không đủ", null);
//                    }
                }
              }
            return new BaseResponse(200, "OK", null);
        } catch (Exception e) {
            return new BaseResponse(500, "Internal Server Error", null);
        }
    }
//    public boolean calculatorMaterial(Materials material, TreatmentCardMaterialReq materialReq, List<TreatmentHistoryMaterial> treatmentCardMaterial, boolean create,Long quantityPet) {
//        Double materialQuantity = material.getFirstInventory() != null ? Double.parseDouble(material.getFirstInventory()) : 0.0;
//        Double newMaterialQuantity = materialReq.getQuantity() != null ? materialReq.getQuantity() * quantityPet : 0.0;
//
//        // Kiểm tra nếu treatmentCardMaterial là null
//        if (treatmentCardMaterial != null) {
//            TreatmentHistoryMaterial matchingMaterial = null;
//            for (TreatmentHistoryMaterial thm : treatmentCardMaterial) {
//                if (thm.getMaterialId().equals(materialReq.getMaterialId())) {
//                    matchingMaterial = thm;
//                    break;
//                }
//            }
//
//            if (matchingMaterial != null) {
//                if (!create) {
//                    material.setFirstInventory(String.valueOf(materialQuantity));
//                    materialsRepository.save(material);
//                    return true;
//                } else {
//                    if (materialQuantity >= newMaterialQuantity) {
//                        materialQuantity -= newMaterialQuantity;
//                        material.setFirstInventory(String.valueOf(materialQuantity));
//                        materialsRepository.save(material);
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            }
//        }
//        // Nếu treatmentCardMaterial là null hoặc không tìm thấy matchingMaterial
//        if (materialQuantity >= newMaterialQuantity) {
//            materialQuantity -= newMaterialQuantity;
//            material.setFirstInventory(String.valueOf(materialQuantity));
//            materialsRepository.save(material);
//            return true;
//        } else {
//            return false;
//        }
//    }


    @Override
    @Transactional
    public BaseResponse updateTreatmentHistory(Long treatmentCardId, List<TreatmentHistoryReq> reqList,Long quantityPet) {
        List<TreatmentHistory> treatmentHistoryList = treatmentHistoryRepository.findByTreatmentCardId(treatmentCardId);
        List<TreatmentHistoryMaterial> existTreatmentHistoryMaterials = new ArrayList<>();
        for (var treatmentHistory : treatmentHistoryList) {
            existTreatmentHistoryMaterials.addAll(treatmentHistoryMaterialRepository.findByTreatmentHistoryId(treatmentHistory.getId()));
        }

        treatmentHistoryRepository.deleteAll(treatmentHistoryList);
        treatmentHistoryList.clear();
        // Update the properties
        for(var req:reqList) {
            TreatmentHistory treatmentHistory = new TreatmentHistory();
            treatmentHistory.setStatus(1);
            treatmentHistory.setTreatmentCardId(req.getTreatmentCardId());
            treatmentHistory.setCheckingDate(req.getCheckingDate());
            treatmentHistory.setUpdatedBy(req.getUpdatedBy());
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
//                if(!calculatorMaterial(material,materialReq,existTreatmentHistoryMaterials,false,quantityPet)){
//                    return new BaseResponse(500, "Số lượng trong kho không đủ", null);
//                }
                TreatmentHistoryMaterial treatmentCardMaterial = new TreatmentHistoryMaterial();
                treatmentCardMaterial.setTreatmentHistoryId(treatmentHistory.getId());
                treatmentCardMaterial.setMaterialId(material.getId());
                treatmentCardMaterial.setQuantity(materialReq.getQuantity());
                treatmentHistoryMaterialRepository.save(treatmentCardMaterial);
            }
        }
        return new BaseResponse(200, "OK", treatmentHistoryList);
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
