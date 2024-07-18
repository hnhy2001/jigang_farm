package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ChangeCageReq;
import com.example.jingangfarmmanagement.model.req.ChangeStatusPetReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.UpdateWeightPetReq;
import com.example.jingangfarmmanagement.projection.FarmProjection;
import com.example.jingangfarmmanagement.projection.PetProjection;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.ChangeCageHistoryRepository;
import com.example.jingangfarmmanagement.repository.PetRepository;
import com.example.jingangfarmmanagement.repository.entity.ChangeCageHistory;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.PetService;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import com.example.jingangfarmmanagement.uitl.ObjectMapperUtils;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl extends BaseServiceImpl<Pet> implements PetService {
    private static final String DELETED_FILTER = ";status>-1";

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private ChangeCageHistoryRepository changeCageHistoryRepository;

    @Override
    protected BaseRepository<Pet> getRepository() {
        return petRepository;
    }

    @Override
    public Page<PetProjection> customSearch(SearchReq req) {
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<Pet> spec = rootNode.accept(new CustomRsqlVisitor<>());
        Pageable pageable = getPage(req);
        return petRepository.findAll(spec, PetProjection.class, pageable);
    }

    @Override
    public PetProjection customDetails(Long id) {
        return null;
    }

    @Override
    public List<Pet> getByStatus(int status) {
        return petRepository.findAllByStatus(1);
    }


    @Override
    @Transactional
    public BaseResponse changeCage(ChangeCageReq changeCageReq) {
        List<Pet> pets = petRepository.findByIdIn(changeCageReq.getPetList());
        List<ChangeCageHistory> changeHistories = pets.stream().map(pet -> {
            ChangeCageHistory changeCageHistory = new ChangeCageHistory();
            changeCageHistory.setPet(pet);
            changeCageHistory.setStatus(1);
            changeCageHistory.setFarmNameFrom(pet.getCage().getFarm().getName());
            changeCageHistory.setFarmNameTo(changeCageReq.getCage().getFarm().getName());
            changeCageHistory.setCageNameFrom(pet.getCage().getName());
            changeCageHistory.setCageNameTo(changeCageReq.getCage().getName());
            changeCageHistory.setCreateDate(DateUtil.getCurrenDateTime());
            changeCageHistory.setNote(changeCageReq.getNote());
            pet.setCage(changeCageReq.getCage());
            return changeCageHistory;
        }).collect(Collectors.toList());

        changeCageHistoryRepository.saveAll(changeHistories);
        petRepository.saveAll(pets);
        return new BaseResponse(200, "OK", "Chuyển chuồng thành công");
    }

    @Override
    public BaseResponse createPet(Pet pet) throws Exception {
        pet.setStatus(Status.ACTIVE);
        pet.setCreateDate(DateUtil.getCurrenDateTime());
        pet.setUpdateHeathDate(DateUtil.getCurrenDateTime());
        pet.setLastDateUpdate(DateUtil.getCurrenDateTime());
        pet.setPregnantDateUpdate(DateUtil.getCurrenDateTime());
        if (pet.getUilness() == null || pet.getUilness().isBlank()) {
            pet.setPetCondition(2);
        } else {
            pet.setPetCondition(1);
        }
        pet.setName(generateName(pet.getSex()));
        return new BaseResponse(200, "Thêm mới vật nuôi thành công", petRepository.save(pet));
    }

    @Override
    public BaseResponse updatePet(Pet pet) throws Exception {
        Pet entityMy = petRepository.getById(pet.getId());
        Pet existingPet = petRepository.findByCageAndFarmAndName(pet.getName(), entityMy.getCage().getName(), entityMy.getCage().getFarm().getName());
        if (existingPet != null && !existingPet.getId().equals(pet.getId())) {
            return new BaseResponse(500, "Tên vật nuôi đã tồn tại", null);
        }
        ObjectMapperUtils.map(pet, entityMy);
        pet.setUpdateDate(DateUtil.getCurrenDateTime());
        pet.setUpdateHeathDate(DateUtil.getCurrenDateTime());
        pet.setLastDateUpdate(DateUtil.getCurrenDateTime());
        pet.setPregnantDateUpdate(DateUtil.getCurrenDateTime());
        pet.setPetCondition(1);
        if (pet.getUilness() == null || pet.getUilness().isBlank()) {
            pet.setStatus(2);
        } else {
            pet.setStatus(1);
        }
        return new BaseResponse(200, "Cập nhật vật nuôi thành công", petRepository.save(entityMy));
    }

    @Override
    public BaseResponse updatePetWeight(UpdateWeightPetReq updateWeightPet) {
        List<Pet> pets = petRepository.findByIdIn(updateWeightPet.getPetIds());
        for (Pet pet : pets) {
            pet.setWeight(updateWeightPet.getWeight());
        }
        return new BaseResponse(200, "Cập nhật cân nặng vật nuôi thành công", petRepository.saveAll((pets)));
    }

    @Override
    public BaseResponse updatePetStatus(List<ChangeStatusPetReq> changeStatusPetReqs) {
        List<Pet> pets = new ArrayList<>();
        for (var changeStatusPetReq : changeStatusPetReqs) {
            Optional<Pet> pet = petRepository.findById(changeStatusPetReq.getPetId());
            if (pet.isPresent()) {
                pet.get().setStatus(changeStatusPetReq.getStatus());
                pet.get().setNote(changeStatusPetReq.getNote());
                pet.get().setUpdateDate(DateUtil.getCurrenDateTime());
                pet.get().setPregnantDateUpdate(changeStatusPetReq.getPregnantDateUpdate());
                pets.add(pet.get());
            }
        }
        return new BaseResponse(200, "Cập nhật trạng thái vật nuôi thành công", petRepository.saveAll((pets)));
    }

    @Override
    public BaseResponse updatePetHealthCondition(List<ChangeStatusPetReq> changeStatusPetReqs) {
        List<Pet> pets = new ArrayList<>();
        for (var changeStatusPetReq : changeStatusPetReqs) {
            Optional<Pet> pet = petRepository.findById(changeStatusPetReq.getPetId());
            if (pet.isPresent()) {
                pet.get().setUpdateDate(DateUtil.getCurrenDateTime());
                pet.get().setUpdateHeathDate(DateUtil.getCurrenDateTime());
                pet.get().setPetCondition(changeStatusPetReq.getPetCondition());
                pet.get().setUpdateHeathDate(DateUtil.getCurrenDateTime());
                pets.add(pet.get());
            }
        }
        return new BaseResponse(200, "Cập nhật trạng thái vật nuôi thành công", petRepository.saveAll((pets)));
    }

    @Override
    public Pet getMenPetByNumbersOfMonth(String month) {
        return petRepository.findMenPetByNunbersOfMonth(month);
    }

    @Override
    public Pet getWomenPetByNumbersOfMonth(String month) {
        return petRepository.findWomenPetByNunbersOfMonth(month);
    }

    @Override
    public BaseResponse findPetWithCageAndFarm(List<Long> cageIds, List<Long> farmIds, Long startDate, Long endDate) {
        List<Pet> pets = new ArrayList<>();
        List<Pet> resultPets = new ArrayList<>();
        if (cageIds!=null) {
            for (var cageId : cageIds) {
                pets = petRepository.findByCageId(cageId, startDate, endDate);
                resultPets.addAll(pets);
            }
        } else {
            for (var farmId : farmIds) {
                pets = petRepository.findByFarmId(farmId, startDate, endDate);
                resultPets.addAll(pets);
            }
        }

        return new BaseResponse(200, "Lấy vật nuôi thành công", resultPets);
    }

    public String generateName(int sex) {
        String year = DateUtil.getYearNow();
        String month = DateUtil.getMonthNow();
        if (sex == 1) {
            Pet pet = this.getMenPetByNumbersOfMonth(year + month);
            if (pet == null) {
                return year + month + "001";
            } else {
                return year + month + getNumber(Integer.parseInt(String.valueOf(pet.getName().substring(5, 7))) + 2);
            }
        } else {
            Pet pet = this.getWomenPetByNumbersOfMonth(year + month);
            if (pet == null) {
                return year + month + "002";
            } else {
                return year + month + getNumber(Integer.parseInt(String.valueOf(pet.getName().substring(5, 7)))  + 2);
            }
        }
    }

    public String getNumber(int number) {
        if (number < 10) {
            return "00" + number;
        }

        if (number < 100) {
            return "0" + number;
        }

        return String.valueOf(number);
    }
}
