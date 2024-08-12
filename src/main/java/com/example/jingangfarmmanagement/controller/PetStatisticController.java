package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.ExportPetReq;
import com.example.jingangfarmmanagement.model.response.ExportPetRes;
import com.example.jingangfarmmanagement.repository.dto.PetDeathStatisticDto;
import com.example.jingangfarmmanagement.repository.dto.PetStatisticDto;
import com.example.jingangfarmmanagement.repository.entity.ExportPet;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.ExportPetService;
import com.example.jingangfarmmanagement.service.Impl.PetStatisticImpl;
import com.example.jingangfarmmanagement.service.PetStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("pet-statistic")
public class PetStatisticController  {
    @Autowired
    PetStatisticImpl petStatisticService;



    @GetMapping("/total")
    public PetStatisticDto petStatistic(
                                        @RequestParam(required = false) Long endDate,
                                        @RequestParam(required = false) List<Integer> sex,
                                        @RequestParam(required = false) List<Integer> status,
                                        @RequestParam(required = false) List<Long> cageId,
                                        @RequestParam(required = false) List<Long> farmId,
                                        @RequestParam(required = false) Integer age) {  // Change int to Integer
        return petStatisticService.petStatistic( endDate, sex, status, cageId, farmId, age);
    }

    @GetMapping("/total/death")
    public List<PetDeathStatisticDto> petStatisticDeath(@RequestParam(required = false) Long startDate,
                                                        @RequestParam(required = false) Long endDate,
                                                        @RequestParam(required = false) List<Integer> sex,
                                                        @RequestParam(required = false)  List<Integer> status,
                                                        @RequestParam(required = false) List<Long> cageId,
                                                        @RequestParam(required = false) List<Long> farmId,
                                                        @RequestParam(required = false) Integer age) {
        return petStatisticService.getPetDeathPerDay(startDate,endDate,sex,status,cageId,farmId,age);
    }
    @GetMapping("/total/born")
    public List<PetDeathStatisticDto> petStatisticBorn(@RequestParam(required = false) Long startDate,
                                                        @RequestParam(required = false) Long endDate,
                                                        @RequestParam(required = false) List<Integer> sex,
                                                        @RequestParam(required = false)  List<Integer> status,
                                                        @RequestParam(required = false) List<Long> cageId,
                                                        @RequestParam(required = false) List<Long> farmId,
                                                        @RequestParam(required = false) Integer age) {
        return petStatisticService.getPetBornPerDay(startDate,endDate,sex,status,cageId,farmId,age);
    }
    @GetMapping("/sync")
    public void syncDateOfBirth() {
        petStatisticService.syncDateOfBirth();
    }

}
