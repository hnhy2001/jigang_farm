package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.MealVoucherReq;
import com.example.jingangfarmmanagement.model.req.TreatmentCardReq;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.repository.entity.MealVoucher;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import com.example.jingangfarmmanagement.repository.entity.Uilness;
import com.example.jingangfarmmanagement.service.MealVoucherService;
import com.example.jingangfarmmanagement.service.TreatmentCardService;
import com.example.jingangfarmmanagement.uitl.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class MealVoucherServiceImpl extends BaseServiceImpl<MealVoucher> implements MealVoucherService {
    @Autowired
    MealVoucherRepository mealVoucherRepository;
    @Autowired
    PetRepository petRepository;
    @Override
    protected BaseRepository<MealVoucher> getRepository() {
        return mealVoucherRepository;
    }
    @Override
    public BaseResponse createMealVoucher(MealVoucherReq req) {
        try {
            MealVoucher mealVoucher = new MealVoucher();
            mealVoucher.setCode(req.getCode());
            mealVoucher.setNote(req.getNote());
            mealVoucher.setStatus(1);
            mealVoucher.setCreateDate(req.getCreateDate());
            List<Pet> pets = new ArrayList<>();
            for (var petId : req.getPetIds()) {
                Pet pet = petRepository.findById(petId).orElseThrow();
                pets.add(pet);
            }
            mealVoucher.setPets(pets);
            mealVoucherRepository.save(mealVoucher);
            return new BaseResponse(200, "OK", mealVoucher);
        } catch (Exception e) {
            return new BaseResponse(500, "Internal Server Error", null);
        }
    }
    @Override
    @Transactional
    public BaseResponse updateMealVoucher(Long mealVoucherId, MealVoucherReq req) {
        MealVoucher mealVoucher = mealVoucherRepository.findById(mealVoucherId)
                .orElseThrow(() -> new EntityNotFoundException("Treatment card not found"));

        // Update the properties
        mealVoucher.setCode(req.getCode());
        mealVoucher.setNote(req.getNote());
        mealVoucher.setStatus(1);
        mealVoucher.setCreateDate(req.getCreateDate());
        List<Pet> existingPets = mealVoucher.getPets();
        existingPets.clear();
        for (var petId : req.getPetIds()) {
            Pet pet = petRepository.findById(petId)
                    .orElseThrow(() -> new EntityNotFoundException(Constant.PET_NOT_FOUND));
            existingPets.add(pet);
        }
        mealVoucher.setPets(existingPets);
        mealVoucherRepository.save(mealVoucher);
        return new BaseResponse(200, "OK", mealVoucher);
    }
    @Override
    public Page<MealVoucher> findMealVoucherHistoriesByPet(List<Long> petIds, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mealVoucherRepository.findMealVoucherByPet(petIds, pageable);
    }
}
