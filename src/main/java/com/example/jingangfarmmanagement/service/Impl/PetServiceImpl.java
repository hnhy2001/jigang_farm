package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ChangeCageReq;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl extends BaseServiceImpl<Pet> implements PetService {
    private static final String DELETED_FILTER = ";status>-1";

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private ChangeCageHistoryRepository changeCageHistoryRepository;
    @Override
    protected BaseRepository<Pet> getRepository() {return petRepository;
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
        if (petRepository.existsByName(pet.getName())) {
            return new BaseResponse(500, "Tên vật nuôi đã tồn tại", null);
        }
        return new BaseResponse(200, "Thêm mới vật nuôi thành công", petRepository.save(pet));
    }
    @Override
    public BaseResponse updatePet (Pet pet) throws Exception {
        Pet entityMy = petRepository.getById(pet.getId());
        Pet existingPet = petRepository.findByName(pet.getName());
        if (existingPet != null && !existingPet.getId().equals(pet.getId())) {
            return new BaseResponse(500, "Tên vật nuôi đã tồn tại", null);
        }
        ObjectMapperUtils.map(pet, entityMy);
        pet.setUpdateDate(DateUtil.getCurrenDateTime());
        return new BaseResponse(200, "Cập nhật vật nuôi thành công", petRepository.save(entityMy));
    }

    @Override
    public BaseResponse updatePetWeight(UpdateWeightPetReq updateWeightPet)  {
        List<Pet> pets = petRepository.findByIdIn(updateWeightPet.getPetIds());
        for(Pet pet: pets) {
            pet.setWeight(updateWeightPet.getWeight());
        }
        return new BaseResponse(200, "Cập nhật cân nặng vật nuôi thành công",   petRepository.saveAll((pets)));
    }
    @Override
    public BaseResponse findPetWithCageAndFarm(Long cageId, Long farmId, Long startDate, Long endDate){
        List<Pet> pets =petRepository.findByCageIdFarmId(cageId,farmId,startDate,endDate);
        return new BaseResponse(200, "Lấy vật nuôi thành công",pets);
    }
}
