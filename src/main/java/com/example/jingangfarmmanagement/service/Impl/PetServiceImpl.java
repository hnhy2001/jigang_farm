package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ChangeCageReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.projection.FarmProjection;
import com.example.jingangfarmmanagement.projection.PetProjection;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.PetRepository;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.PetService;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl extends BaseServiceImpl<Pet> implements PetService {
    private static final String DELETED_FILTER = ";status>-1";

    @Autowired
    private PetRepository petRepository;
    @Override
    protected BaseRepository<Pet> getRepository() {return petRepository;
    }

    @Override
    public Page<PetProjection> customSearch(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
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
    public BaseResponse changeCage(ChangeCageReq changCageReq) {
        List<Pet> pets = petRepository.findByIdIn(changCageReq.getPetList());
        pets.stream().map(e -> {
            e.setCage(changCageReq.getCage());
            return e;
        }).collect(Collectors.toList());
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
}
