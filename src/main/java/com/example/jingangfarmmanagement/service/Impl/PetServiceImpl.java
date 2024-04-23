package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ChangeCageReq;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.PetRepository;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl extends BaseServiceImpl<Pet> implements PetService {
    @Autowired
    private PetRepository petRepository;
    @Override
    protected BaseRepository<Pet> getRepository() {
        return petRepository;
    }

    @Override
    public BaseResponse changeCage(ChangeCageReq changCageReq) {
        List<Pet> pets = changCageReq.getPetList().stream().map(e -> {
            e.setCage(changCageReq.getCage());
            return e;
        }).collect(Collectors.toList());
        petRepository.saveAll(pets);
        return new BaseResponse(200, "OK", "Chuyển chuồng thành công");
    }
}
