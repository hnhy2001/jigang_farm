package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.response.QuantityPetRes;
import com.example.jingangfarmmanagement.projection.CageProjection;
import com.example.jingangfarmmanagement.projection.FarmProjection;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.CageRepository;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.CageService;
import com.example.jingangfarmmanagement.service.PetService;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CageServiceImpl extends BaseServiceImpl<Cage> implements CageService {
    private static final String DELETED_FILTER = ";status>-1";

    @Autowired
    private CageRepository cageReponsitory;

    @Autowired
    private PetService petService;

    @Override
    protected BaseRepository<Cage> getRepository() {
        return cageReponsitory;
    }

    @Override
    public Page<CageProjection> customSearch(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<Cage> spec = rootNode.accept(new CustomRsqlVisitor<>());
        Pageable pageable = getPage(req);
        Page<CageProjection> result = cageReponsitory.findAll(spec, CageProjection.class, pageable);
        return result;
    }

    @Override
    public BaseResponse quantityPet() {
        ModelMapper mapper = new ModelMapper();
        List<Cage> cageList = super.getAll();
        List<Pet> petList = petService.getAll();
        if (cageList.isEmpty())
            return new BaseResponse().fail("Không có chuồng nào");
        if (petList.isEmpty())
            return new BaseResponse().fail("Không có con vật nào");
        List<QuantityPetRes> result = cageList.stream()
                .filter(f -> f.getStatus() == 1)
                .map(e -> mapper.map(e, QuantityPetRes.class)).collect(Collectors.toList());
        result.stream().forEach(res -> {
            petList.stream().forEach(pet -> {
                if (pet.getCage().equals(res.getCage()))
                    res.setQuantity(res.getQuantity() + 1L);
            });
        });
        return new BaseResponse().success(result);
    }
}
