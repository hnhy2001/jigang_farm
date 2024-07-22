package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ExportPetReq;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.response.ExportPetRes;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.ExportPetRepository;
import com.example.jingangfarmmanagement.repository.PetRepository;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.service.ExportPetService;
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
public class ExportPetServiceImpl extends BaseServiceImpl<ExportPet> implements ExportPetService {
    private static final String DELETED_FILTER =";status>-1" ;
    @Autowired
    ExportPetRepository exportPetRepository;
    @Autowired
    PetRepository petRepository;
    @Override
    protected BaseRepository<ExportPet> getRepository() {
        return exportPetRepository;
    }
    @Override
    @Transactional
    public BaseResponse createExportPet(ExportPetReq req){
        List<ExportPet> exportPets= req.getPetIds().stream().map(petId->{
            Optional<Pet> petOptional = petRepository.findById(petId);
            if (petOptional.isPresent()) {
                Pet pet = petOptional.get();
                pet.setStatus(-1);
                petRepository.save(pet);
            }
            ExportPet exportPet  =new ExportPet(petId, req.getReasonExport(), req.getExportDate(), req.getType(),req.getNote(),new ArrayList<>());
            exportPet.setStatus(1);
            return  exportPet;
        }).collect(Collectors.toList());
        exportPetRepository.saveAll(exportPets);
        return new BaseResponse(200,"OK", exportPets);
    }

    @Override
    public BaseResponse getExportPetById(Long id){
        ExportPet exportPet = exportPetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Export Pet not found"));
        Optional<Pet> pet = petRepository.findById(exportPet.getPetId());

        ExportPetRes exportPetRes = new ExportPetRes();
        exportPetRes.setExportDate(exportPet.getExportDate());
        exportPetRes.setReasonExport(exportPet.getReasonExport());
        exportPetRes.setExportDate(exportPet.getExportDate());
        exportPetRes.setNote(exportPet.getNote());
        exportPetRes.setType(exportPet.getType());
        exportPetRes.setPet(pet.orElse(null));
        return new BaseResponse(200, "OK", exportPetRes);
    }
    @Override
    public Page<ExportPetRes> searchExportPet(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<ExportPet> spec = rootNode.accept(new CustomRsqlVisitor<>());
        Pageable pageable = getPage(req);
        Page<ExportPet> exportPets = exportPetRepository.findAll(spec, pageable);

        List<ExportPetRes> exportPetResList = exportPets.getContent().stream()
                .map(exportPet -> {
                    Optional<Pet> pet = petRepository.findById(exportPet.getPetId());

                    ExportPetRes exportPetRes = new ExportPetRes();
                    exportPetRes.setExportDate(exportPet.getExportDate());
                    exportPetRes.setReasonExport(exportPet.getReasonExport());
                    exportPetRes.setExportDate(exportPet.getExportDate());
                    exportPetRes.setNote(exportPet.getNote());
                    exportPetRes.setType(exportPet.getType());
                    exportPetRes.setPet(pet.orElse(null));
                    return exportPetRes;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(exportPetResList, pageable, exportPets.getTotalElements());
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
    @Override
    public BaseResponse getDeathPetByFarmAndCage(Long cageId){
        List<ExportPetStatistic> exportPetStatistics=exportPetRepository.statisticExportPetWithCage(cageId);
        return new BaseResponse(200,"OK",exportPetStatistics);
    }
    @Override
    public Page<ExportPetRes> searchExportPet(Long cageId, String name, String code, Integer sex, Integer age, Long startExportDate,Long endExportDate, String note, Pageable pageable) {
        Page<ExportPet> exportPets = exportPetRepository.searchExportPet(cageId, name, code, sex, age, startExportDate,endExportDate, note, pageable);
        List<ExportPetRes> exportPetResList = exportPets.getContent().stream().map(exportPet -> {
            Optional<Pet> pet = petRepository.findById(exportPet.getPetId());
            ExportPetRes exportPetRes = new ExportPetRes();
            exportPetRes.setExportDate(exportPet.getExportDate());
            exportPetRes.setReasonExport(exportPet.getReasonExport());
            exportPetRes.setExportDate(exportPet.getExportDate());
            exportPetRes.setNote(exportPet.getNote());
            exportPetRes.setType(exportPet.getType());
            exportPetRes.setPet(pet.orElse(null));
            return exportPetRes;
        }).collect(Collectors.toList());
        return new PageImpl<>(exportPetResList, pageable, exportPets.getTotalElements());
    }
}
