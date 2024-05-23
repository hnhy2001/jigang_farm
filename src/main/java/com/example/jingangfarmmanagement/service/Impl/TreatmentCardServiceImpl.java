package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.TreatmentCardReq;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.service.TreatmentCardService;
import com.example.jingangfarmmanagement.uitl.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    TreatmentCardMaterialRepository treatmentCardMaterialRepository;
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
                treatmentCardMaterial.setTreatmentCard(treatmentCard);
                treatmentCardMaterial.setMaterial(material);
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
        List<TreatmentCardMaterial> existingMaterials = treatmentCardMaterialRepository.findByTreatmentCard(treatmentCard);
        treatmentCardMaterialRepository.deleteAll(existingMaterials);
        existingMaterials.clear();
        for (var materialReq : req.getMaterials()) {
            Materials material = materialsRepository.findById(materialReq.getMaterialId())
                    .orElseThrow(() -> new EntityNotFoundException(Constant.MATERIAL_NOT_FOUND));
            TreatmentCardMaterial treatmentCardMaterial = new TreatmentCardMaterial();
            treatmentCardMaterial.setTreatmentCard(treatmentCard);
            treatmentCardMaterial.setMaterial(material);
            treatmentCardMaterial.setQuantity(materialReq.getQuantity());
            treatmentCardMaterialRepository.save(treatmentCardMaterial);
        }
        return new BaseResponse(200, "OK", treatmentCard);
    }

}
