package com.example.jingangfarmmanagement.service;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.*;
import com.example.jingangfarmmanagement.projection.PetProjection;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface PetService extends BaseService<Pet> {
    public BaseResponse changeCage(ChangeCageReq changCageReq);

    Page<PetProjection> customSearch(SearchReq req);

    PetProjection customDetails(Long id);

    List<Pet> getByStatus(int status);

    public BaseResponse createPet(Pet pet) throws Exception;
    public BaseResponse updatePet(Pet pet) throws Exception;

    public BaseResponse updatePetWeight(UpdateWeightPetReq updateWeightPet) ;
    public BaseResponse findPetWithCageAndFarm(List<Long> cageId,List<Long> farmId, Long startDate, Long endDate);

    public BaseResponse updatePetStatus(List<ChangeStatusPetReq> changeStatusPetReqs);
    public BaseResponse updatePetHealthCondition(List<ChangeStatusPetReq> changeStatusPetReqs);
    public Pet getMenPetByNumbersOfMonth(String month);
    public Pet getWomenPetByNumbersOfMonth(String month);
    public BaseResponse statisticQuantityUnilness(StatisticQuantityUnilessReq req);
    public BaseResponse statisticQuantityUnilnessType(StatisticQuantityUnilessReq req);
    public BaseResponse findAllPet();
    public BaseResponse changeCageByName(ChangeCageByNameReq changeCageReq);
    public void exportToExcel( List<Pet> pets,String filePath) throws IOException;

}
