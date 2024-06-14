package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.mapper.TreatmentCardMapper;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.MealVoucherHistoryReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.TreatmentHistoryReq;
import com.example.jingangfarmmanagement.model.response.MaterialRes;
import com.example.jingangfarmmanagement.model.response.MealVoucherHistoryRes;
import com.example.jingangfarmmanagement.model.response.TreatmentHistoryRes;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.service.MealVoucherHistoryService;
import com.example.jingangfarmmanagement.service.TreatmentHistoryService;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealVoucherHistoryServiceImpl extends BaseServiceImpl<MealVoucherHistory> implements MealVoucherHistoryService {
    private static final String DELETED_FILTER =";status>-1" ;
    @Autowired
    MealVoucherHistoryRepository mealVoucherHistoryRepository;
    @Autowired
    MaterialsRepository materialsRepository;
    @Autowired
    MealVoucherHistoryMaterialRepository mealVoucherHistoryMaterialRepository;
    @Autowired
    TreatmentCardMapper treatmentCardMapper;
    @Override
    protected BaseRepository<MealVoucherHistory> getRepository() {
        return mealVoucherHistoryRepository;
    }
    @Override
    public BaseResponse createMealVoucherHistory(List<MealVoucherHistoryReq> reqList) {
        try {
            for(var req:reqList) {
                MealVoucherHistory mealVoucherHistory = new MealVoucherHistory();
                mealVoucherHistory.setStatus(1);
                mealVoucherHistory.setMealVoucherId(req.getMealVoucherId());
                mealVoucherHistory.setCheckingDate(req.getCheckingDate());
                mealVoucherHistoryRepository.save(mealVoucherHistory);
                for (var materialReq : req.getMaterials()) {
                    Materials material = materialsRepository.findById(materialReq.getMaterialId())
                            .orElseThrow();
                    MealVoucherHistoryMaterial mealVoucherHistoryMaterial = new MealVoucherHistoryMaterial();
                    mealVoucherHistoryMaterial.setMealVoucherHistoryId(mealVoucherHistory.getId());
                    mealVoucherHistoryMaterial.setMaterialId(material.getId());
                    mealVoucherHistoryMaterial.setQuantity(materialReq.getQuantity());
                    mealVoucherHistoryMaterialRepository.save(mealVoucherHistoryMaterial);
                }
              }
            return new BaseResponse(200, "OK", null);
        } catch (Exception e) {
            return new BaseResponse(500, "Internal Server Error", null);
        }
    }
    @Override
    @Transactional
    public BaseResponse updateMealVoucherHistory(Long mealVoucherHistoryId, List<MealVoucherHistoryReq> reqList) {
        List<MealVoucherHistory> mealVoucherHistoryList = mealVoucherHistoryRepository.findByMealVoucherId(mealVoucherHistoryId);
        mealVoucherHistoryRepository.deleteAll(mealVoucherHistoryList);
        mealVoucherHistoryList.clear();
        // Update the properties
        for(var req:reqList) {
            MealVoucherHistory mealVoucherHistory = new MealVoucherHistory();
            mealVoucherHistory.setStatus(1);
            mealVoucherHistory.setMealVoucherId(req.getMealVoucherId());
            mealVoucherHistory.setCheckingDate(req.getCheckingDate());
            mealVoucherHistoryRepository.save(mealVoucherHistory);
            for (var materialReq : req.getMaterials()) {
                Materials material = materialsRepository.findById(materialReq.getMaterialId())
                        .orElseThrow();
                MealVoucherHistoryMaterial mealVoucherHistoryMaterial = new MealVoucherHistoryMaterial();
                mealVoucherHistoryMaterial.setMealVoucherHistoryId(mealVoucherHistory.getId());
                mealVoucherHistoryMaterial.setMaterialId(material.getId());
                mealVoucherHistoryMaterial.setQuantity(materialReq.getQuantity());
                mealVoucherHistoryMaterialRepository.save(mealVoucherHistoryMaterial);
            }
        }
        return new BaseResponse(200, "OK", mealVoucherHistoryList);
    }
    @Override
    public BaseResponse getMealVoucherHistoryById(Long id){
        MealVoucherHistory mealVoucherHistory = mealVoucherHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal voucher History not found"));

        List<MealVoucherHistoryMaterial> mealVoucherHistoryMaterials = mealVoucherHistoryMaterialRepository.findByMealVoucherHistoryId(id);

        List<MaterialRes> materialList = mealVoucherHistoryMaterials.stream()
                .map(treatmentCardMaterial ->{
                    Materials material = materialsRepository.findById(treatmentCardMaterial.getMaterialId())
                            .orElseThrow(EntityNotFoundException::new);
                    return treatmentCardMapper.toMaterialRes(material, treatmentCardMaterial.getQuantity());
                })
                .collect(Collectors.toList());

        MealVoucherHistoryRes mealVoucherHistoryRes = new MealVoucherHistoryRes();
        mealVoucherHistoryRes.setMealVoucherHistory(mealVoucherHistory);
        mealVoucherHistoryRes.setMaterial(materialList);
        return new BaseResponse(200, "OK", mealVoucherHistoryRes);
    }
    @Override
    public Page<MealVoucherHistoryRes> searchMealVoucherHistoryHistory(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<MealVoucherHistory> spec = rootNode.accept(new CustomRsqlVisitor<>());
        Pageable pageable = getPage(req);
        Page<MealVoucherHistory> mealVoucherHistories = mealVoucherHistoryRepository.findAll(spec, pageable);

        List<MealVoucherHistoryRes> historyResList = mealVoucherHistories.getContent().stream()
                .map(mealVoucherHistory -> {
                    List<MealVoucherHistoryMaterial> mealVoucherMaterials = mealVoucherHistoryMaterialRepository.findByMealVoucherHistoryId(mealVoucherHistory.getId());
                    List<MaterialRes> materialList = mealVoucherMaterials.stream()
                            .map(mealVoucherMaterial ->{
                                Materials material = materialsRepository.findById(mealVoucherMaterial.getMaterialId())
                                        .orElseThrow(EntityNotFoundException::new);
                                return treatmentCardMapper.toMaterialRes(material, mealVoucherMaterial.getQuantity());
                            })
                            .collect(Collectors.toList());

                    MealVoucherHistoryRes mealVoucherHistoryRes = new MealVoucherHistoryRes();
                    mealVoucherHistoryRes.setMealVoucherHistory(mealVoucherHistory);
                    mealVoucherHistoryRes.setMaterial(materialList);
                    return mealVoucherHistoryRes;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(historyResList, pageable, mealVoucherHistories.getTotalElements());
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
