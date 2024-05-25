package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.mapper.TreatmentCardMapper;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.TreatmentCardReq;
import com.example.jingangfarmmanagement.model.response.MaterialRes;
import com.example.jingangfarmmanagement.model.response.TreatmentCardRes;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.service.TreatmentCardService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TreatmentCardServiceImpl extends BaseServiceImpl<TreatmentCard> implements TreatmentCardService {
    @Autowired
    TreatmentCardRepository treatmentCardRepository;
    @Autowired
    UilnessRepository uilnessRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    MaterialsRepository materialsRepository;
    @Autowired
    TreatmentCardMapper treatmentCardMapper;
    @Autowired
    TreatmentCardMaterialRepository treatmentCardMaterialRepository;
    private static final String DELETED_FILTER = ";status>-1";
    @Override
    protected BaseRepository<TreatmentCard> getRepository() {
        return treatmentCardRepository;
    }
    @Override
    public BaseResponse createTreatment(TreatmentCardReq req) {
        try {
            TreatmentCard treatmentCard = new TreatmentCard();
            treatmentCard.setCode(req.getCode());
            treatmentCard.setNote(req.getCode());
            treatmentCard.setStatus(1);
            treatmentCard.setCreateDate(req.getCreateDate());
            List<Uilness> uilnesses = new ArrayList<>();
            for (var ulinessId : req.getUlinessIds()) {
                Uilness uilness = uilnessRepository.findById(ulinessId)
                        .orElseThrow();
                uilnesses.add(uilness);
            }
            treatmentCard.setUilnesses(uilnesses);
            List<Pet> pets = new ArrayList<>();
            for (var petId : req.getPetIds()) {
                Pet pet = petRepository.findById(petId).orElseThrow();
                pets.add(pet);
            }
            treatmentCard.setPets(pets);
            treatmentCardRepository.save(treatmentCard);
            for (var materialReq : req.getMaterials()) {
                Materials material = materialsRepository.findById(materialReq.getMaterialId())
                        .orElseThrow();
                TreatmentCardMaterial treatmentCardMaterial = new TreatmentCardMaterial();
                treatmentCardMaterial.setTreatmentCardId(treatmentCard.getId());
                treatmentCardMaterial.setMaterialId(material.getId());
                treatmentCardMaterial.setQuantity(materialReq.getQuantity());
                treatmentCardMaterialRepository.save(treatmentCardMaterial);
            }

            return new BaseResponse(200, "OK", treatmentCard);
        } catch (Exception e) {
            return new BaseResponse(500, "Internal Server Error", null);
        }
    }
    @Override
    @Transactional
    public BaseResponse updateTreatment(Long treatmentCardId, TreatmentCardReq req) {
        TreatmentCard treatmentCard = treatmentCardRepository.findById(treatmentCardId)
                .orElseThrow(() -> new EntityNotFoundException("Treatment card not found"));

        // Update the properties
        treatmentCard.setCode(req.getCode());
        treatmentCard.setNote(req.getNote());
        treatmentCard.setStatus(1);
        treatmentCard.setCreateDate(req.getCreateDate());
        List<Uilness> existingUilnesses = treatmentCard.getUilnesses();
        existingUilnesses.clear();
        for (var ulinessId : req.getUlinessIds()) {
            Uilness uilness = uilnessRepository.findById(ulinessId)
                    .orElseThrow(() -> new EntityNotFoundException(Constant.ULINESS_NOT_FOUND));
            existingUilnesses.add(uilness);
        }
        treatmentCard.setUilnesses(existingUilnesses);
        List<Pet> existingPets = treatmentCard.getPets();
        existingPets.clear();
        for (var petId : req.getPetIds()) {
            Pet pet = petRepository.findById(petId)
                    .orElseThrow(() -> new EntityNotFoundException(Constant.PET_NOT_FOUND));
            existingPets.add(pet);
        }
        treatmentCard.setPets(existingPets);

        // Save the updated treatment card
        treatmentCardRepository.save(treatmentCard);

        // Update materials
        List<TreatmentCardMaterial> existingMaterials = treatmentCardMaterialRepository.findByTreatmentCardId(treatmentCard.getId());
        treatmentCardMaterialRepository.deleteAll(existingMaterials);
        existingMaterials.clear();
        for (var materialReq : req.getMaterials()) {
            Materials material = materialsRepository.findById(materialReq.getMaterialId())
                    .orElseThrow(() -> new EntityNotFoundException(Constant.MATERIAL_NOT_FOUND));
            TreatmentCardMaterial treatmentCardMaterial = new TreatmentCardMaterial();
            treatmentCardMaterial.setTreatmentCardId(treatmentCard.getId());
            treatmentCardMaterial.setMaterialId(material.getId());
            treatmentCardMaterial.setQuantity(materialReq.getQuantity());
            treatmentCardMaterialRepository.save(treatmentCardMaterial);
        }
        return new BaseResponse(200, "OK", treatmentCard);
    }
    @Override
    public BaseResponse getTreatmentCardById(Long id){
        TreatmentCard treatmentCard = treatmentCardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Treatment card not found"));

        List<TreatmentCardMaterial> treatmentCardMaterials = treatmentCardMaterialRepository.findByTreatmentCardId(id);

        List<MaterialRes> materialList = treatmentCardMaterials.stream()
                .map(treatmentCardMaterial ->{
                    Materials material = materialsRepository.findById(treatmentCardMaterial.getMaterialId())
                        .orElseThrow(EntityNotFoundException::new);
                  return treatmentCardMapper.toMaterialRes(material, treatmentCardMaterial.getQuantity());
                })
                .collect(Collectors.toList());

        TreatmentCardRes treatmentCardRes = new TreatmentCardRes();
        treatmentCardRes.setTreatmentCard(treatmentCard);
        treatmentCardRes.setMaterial(materialList);
        return new BaseResponse(200, "OK", treatmentCardRes);
    }
    @Override
    public Page<TreatmentCardRes> searchTreatmentCard(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<TreatmentCard> spec = rootNode.accept(new CustomRsqlVisitor<TreatmentCard>());
        Pageable pageable = getPage(req);
        Page<TreatmentCard> treatmentCards = treatmentCardRepository.findAll(spec, pageable);

        List<TreatmentCardRes> cardResList = treatmentCards.getContent().stream()
                .map(treatmentCard -> {
                    List<TreatmentCardMaterial> treatmentCardMaterials = treatmentCardMaterialRepository.findByTreatmentCardId(treatmentCard.getId());
                    List<MaterialRes> materialList = treatmentCardMaterials.stream()
                            .map(treatmentCardMaterial ->{
                                Materials material = materialsRepository.findById(treatmentCardMaterial.getMaterialId())
                                        .orElseThrow(EntityNotFoundException::new);
                                return treatmentCardMapper.toMaterialRes(material, treatmentCardMaterial.getQuantity());
                            })
                            .collect(Collectors.toList());

                    TreatmentCardRes treatmentCardRes = new TreatmentCardRes();
                    treatmentCardRes.setTreatmentCard(treatmentCard);
                    treatmentCardRes.setMaterial(materialList);
                    return treatmentCardRes;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(cardResList, pageable, treatmentCards.getTotalElements());
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
