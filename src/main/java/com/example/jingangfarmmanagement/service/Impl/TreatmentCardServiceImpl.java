package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.TreatmentCardReq;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.service.TreatmentCardService;
import com.example.jingangfarmmanagement.uitl.Constant;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Override
    protected BaseRepository<TreatmentCard> getRepository() {
        return treatmentCardRepository;
    }
    @Override
    public BaseResponse createTreatment(TreatmentCardReq req) {
        try {
            TreatmentCard treatmentCard = new TreatmentCard();
            treatmentCard.setCode(req.getCode());
            treatmentCard.setNote(req.getNote());
            treatmentCard.setResultTypeCard(req.getResultTypeCard());
            treatmentCard.setResultTypeCardDate(req.getResultTypeCard()==1 || req.getResultTypeCard()==2 ? DateUtil.getCurrenDateTime(): 0);
            treatmentCard.setStatus(1);
            treatmentCard.setCreateDate(req.getCreateDate());
            List<Uilness> uilnesses = new ArrayList<>();
            for(var uilnessReq : req.getUlinessName()){
                if (uilnessRepository.findByName(uilnessReq)!=null) {
                    Uilness existUilness = uilnessRepository.findByName(uilnessReq);
                    uilnesses.add(existUilness);           }
                else{
                    Uilness uilness =new Uilness();
                    uilness.setName(uilnessReq);
                    uilness.setScore(1);
                    uilness.setStatus(1);
                    uilnessRepository.save(uilness);
                    uilnesses.add(uilness);
                }}
            treatmentCard.setUilnesses(uilnesses);
            List<Pet> pets = new ArrayList<>();
            for (var petId : req.getPetIds()) {
                Pet pet = petRepository.findById(petId).orElseThrow();
                pets.add(pet);
            }
            treatmentCard.setPets(pets);
            treatmentCardRepository.save(treatmentCard);
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
        treatmentCard.setResultTypeCard(req.getResultTypeCard());
        treatmentCard.setResultTypeCardDate(req.getResultTypeCard()==1 || req.getResultTypeCard()==2 ? DateUtil.getCurrenDateTime(): 0);
        treatmentCard.setCreateDate(req.getCreateDate());
        List<Uilness> existingUilnesses = treatmentCard.getUilnesses();
        existingUilnesses.clear();
        List<Uilness> uilnesses = new ArrayList<>();
        for(var uilnessReq : req.getUlinessName()){
            if (uilnessRepository.findByName(uilnessReq)!=null) {
                Uilness existUilness = uilnessRepository.findByName(uilnessReq);
                uilnesses.add(existUilness);           }
            else{
                Uilness uilness =new Uilness();
                uilness.setName(uilnessReq);
                uilness.setScore(1);
                uilness.setStatus(1);
                uilnessRepository.save(uilness);
                uilnesses.add(uilness);
            }}
        treatmentCard.setUilnesses(uilnesses);
        List<Pet> existingPets = treatmentCard.getPets();
        existingPets.clear();
        for (var petId : req.getPetIds()) {
            Pet pet = petRepository.findById(petId)
                    .orElseThrow(() -> new EntityNotFoundException(Constant.PET_NOT_FOUND));
            existingPets.add(pet);
        }
        treatmentCard.setPets(existingPets);
        treatmentCardRepository.save(treatmentCard);
        return new BaseResponse(200, "OK", treatmentCard);
    }
    @Override
    public Page<TreatmentCard> findTreatmentHistoriesByPet(List<Long> petIds, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return treatmentCardRepository.findTreatmentCardsByPet(petIds, pageable);
    }
}
