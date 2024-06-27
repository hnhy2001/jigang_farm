package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.StatisticPetWithAge;
import com.example.jingangfarmmanagement.model.response.*;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.repository.entity.Uilness;
import com.example.jingangfarmmanagement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    FarmService farmService;

    @Autowired
    CageService cageService;

    @Autowired
    PetService petService;

    @Autowired
    UilnessService uilnessService;

    @Override
    public BaseResponse statisticPetWithAge(StatisticPetWithAge statisticPetWithAge) {
        return new BaseResponse().success("ok");
    }

    @Override
    public BaseResponse statisticByFarm(Long farmId) throws Exception {
        Farm farm = farmService.getById(farmId);
        if (farm == null){
            return new BaseResponse().fail("Không có trại nào tương ứng với id trên");
        }
        List<Cage> cageList = cageService.getByFarm(farm);
        if (cageList.isEmpty()){
            return new BaseResponse().success("Không có chuồng nào thuộc trại này");
        }
        List<Pet> petList = petService.getAll();
//        StatisticByFarmRes result = new StatisticByFarmRes();
//        result.setFarm(farm);
//        result.setValue(setValueStatistic(cageList, petList));

        return new BaseResponse().success(setValueStatistic(cageList, petList));
    }

    public List<StatisticPetByAge> createStatisticByAge(){
        List<StatisticPetByAge> list = new ArrayList<>();
        list.add(new StatisticPetByAge().builder().from(0).to(6).unit("Tháng").quantity(0).build());
        list.add(new StatisticPetByAge().builder().from(6).to(12).unit("Tháng").quantity(0).build());
        list.add(new StatisticPetByAge().builder().from(12).to(9999999).unit("Tháng").quantity(0).build());
        return list;
    }

    public List<StatisticPetByWeight> createStatisticByWeight(){
        List<StatisticPetByWeight> list = new ArrayList<>();
        for (int i = 0; i<10; i++){
            list.add(new StatisticPetByWeight().builder().from(i).to(i+1).unit("Kg").quantity(0).build());
        }
        list.add(new StatisticPetByWeight().builder().from(10).to(99999).unit("Kg").quantity(0).build());
        return list;
    }

    public List<StatisticStatusUilnessPet> createStatisticStatusUilnessPet(){
        List<StatisticStatusUilnessPet> list = new ArrayList<>();
        list.add(new StatisticStatusUilnessPet().builder().point(0).name("Bình thường").totalPet(0).totalMale(0).totalFemale(0).build());
        for (int i = 1; i<=5; i++){
            list.add(new StatisticStatusUilnessPet().builder().point(i).name("Tình trạng bệnh " + i).totalPet(0).totalMale(0).totalFemale(0).build());
        }
        return list;
    }

    public List<ResultStatisticByCageRes> setValueStatistic(List<Cage> cageList, List<Pet> petList){
        List<ResultStatisticByCageRes> resultValueList = new ArrayList<>();
        cageList.forEach(cage -> {
            ResultStatisticByCageRes value = new ResultStatisticByCageRes();
            value.setCage(cage);
            value.setTotalPet(0);
            value.setStatisticPetByAgeList(createStatisticByAge());
            value.setStatisticPetByWeightList(createStatisticByWeight());
            value.setTotalMale(0);
            value.setTotalFemale(0);
            value.setPetDie(0);
            value.setPetLive(0);
            petList.forEach(pet -> {
                if (pet.getCage().equals(cage)){
                    value.setTotalPet(value.getTotalPet() + 1);
                    if (pet.getSex() == 1){
                        value.setTotalMale(value.getTotalMale() + 1);
                    }else {
                        value.setTotalFemale(value.getTotalFemale() + 1);
                    }
                    if (pet.getStatus() == -1){
                        value.setPetDie(value.getPetDie()+1);
                    }else {
                        value.setPetLive(value.getPetLive()+1);
                    }
                    value.getStatisticPetByAgeList().forEach(age -> {
                        if (pet.getAge() >= age.getFrom() && pet.getAge() < age.getTo()){
                            age.setQuantity(age.getQuantity() + 1);
                        }
                    });
                    value.getStatisticPetByWeightList().forEach(weight -> {
                        if (pet.getWeight() >= weight.getFrom() && pet.getWeight() < weight.getTo()){
                            weight.setQuantity(weight.getQuantity() + 1);
                        }
                    });
                }
            });
            resultValueList.add(value);
        });
        return resultValueList;
    }


    @Override
    public BaseResponse statisticFarm(SearchReq req) throws Exception {
        Page<Farm> farmPage = farmService.search(req);
        List<Farm> farmList = farmPage.getContent();
        if (farmList.isEmpty()){
            return new BaseResponse().fail("Không có trại nào!");
        }
        List<Cage> cageList = cageService.getAll();
        if (cageList.isEmpty()){
            return new BaseResponse().success("Không có chuồng nào thuộc trại này");
        }
        List<Uilness> uilnessList = uilnessService.getAll();
        List<Pet> petList = petService.getAll();
        List<StatisticByFarmRes> results = new ArrayList<>();
        farmList.forEach(e -> {
            StatisticByFarmRes result = new StatisticByFarmRes();
            result.setFarm(e);
            result.setTotalFemale(0);
            result.setTotalMale(0);
            result.setTotalPet(0);
            result.setPetLive(0);
            result.setPetDie(0);
//            result.setPetUilness(0);
//            result.setPetNomal(0);
            result.setStatisticPetByAgeList(createStatisticByAge());
            result.setStatisticPetByWeightList(createStatisticByWeight());
            result.setStatisticStatusUilnessPetList(createStatisticStatusUilnessPet());
            getCageList(e, cageList).forEach(cage -> {
                getPetList(cage, petList).forEach(pet -> {
                    result.setTotalPet(result.getTotalPet() + 1);
                    if (pet.getSex() == 1){
                        result.setTotalMale(result.getTotalMale() + 1);
                    }else {
                        result.setTotalFemale(result.getTotalFemale() + 1);
                    }
                    if (pet.getStatus() == -1){
                        result.setPetDie(result.getPetDie()+1);
                    }else {
                        result.setPetLive(result.getPetLive()+1);
                    }
//                    if (pet.getUilness() != null || !pet.getUilness().equals("")){
//                        result.setPetUilness(result.getPetUilness()+1);
//                    }else {
//                        result.setPetNomal(result.getPetNomal()+1);
//                    }
                    result.getStatisticPetByAgeList().forEach(age -> {
                        if (pet.getAge() >= age.getFrom() && pet.getAge() < age.getTo()){
                            age.setQuantity(age.getQuantity() + 1);
                        }
                    });
                    result.getStatisticPetByWeightList().forEach(weight -> {
                        if (pet.getWeight() >= weight.getFrom() && pet.getWeight() < weight.getTo()){
                            weight.setQuantity(weight.getQuantity() + 1);
                        }
                    });
                    if (!uilnessList.isEmpty() && pet.getStatus() != -1){
                        if (pet.getUilness() == null){
                            result.getStatisticStatusUilnessPetList().get(0).setTotalPet(result.getStatisticStatusUilnessPetList().get(0).getTotalPet() + 1);
                            if (pet.getSex() == 1){
                                result.getStatisticStatusUilnessPetList().get(0).setTotalMale(result.getStatisticStatusUilnessPetList().get(0).getTotalMale() + 1);
                            }else {
                                result.getStatisticStatusUilnessPetList().get(0).setTotalFemale(result.getStatisticStatusUilnessPetList().get(0).getTotalFemale() + 1);
                            }
                        }else {
                            String[] uilnesses = pet.getUilness().split(",");
                            AtomicInteger uilnessCheckPoint = new AtomicInteger(0);
                            Arrays.stream(uilnesses).forEach(uilnessCode -> {
                                uilnessList.forEach(uilness -> {
                                    if (uilnessCode.contains(uilness.getCode())){
                                        if (uilness.getScore() >= uilnessCheckPoint.get()){
                                            uilnessCheckPoint.set(uilness.getScore());
                                        }
                                    }
                                });
                            });
                            result.getStatisticStatusUilnessPetList().get(uilnessCheckPoint.get()).setTotalPet(result.getStatisticStatusUilnessPetList().get(uilnessCheckPoint.get()).getTotalPet() + 1);
                            if (pet.getSex() == 1){
                                result.getStatisticStatusUilnessPetList().get(uilnessCheckPoint.get()).setTotalMale(result.getStatisticStatusUilnessPetList().get(uilnessCheckPoint.get()).getTotalMale() + 1);
                            }else {
                                result.getStatisticStatusUilnessPetList().get(uilnessCheckPoint.get()).setTotalFemale(result.getStatisticStatusUilnessPetList().get(uilnessCheckPoint.get()).getTotalFemale() + 1);
                            }
                        }
                    }
                });
            });
            results.add(result);
        });
        Page<StatisticByFarmRes> res = new PageImpl<>(results, farmPage.getPageable(), farmPage.getTotalElements());
        return new BaseResponse().success(res);
    }

    @Override
    public BaseResponse statisticCage(SearchReq req) throws Exception {
        Page<Cage> cagePage = cageService.search(req);
        List<Cage> cageList = cagePage.getContent();
        if (cageList.isEmpty()){
            return new BaseResponse().success("Không có chuồng nào thuộc trại này");
        }
        List<Uilness> uilnessList = uilnessService.getAll();
        List<Pet> petList = petService.getAll();
        List<ResultStatisticByCageRes> results = new ArrayList<>();
            cageList.forEach(cage -> {
                ResultStatisticByCageRes result = new ResultStatisticByCageRes();
                result.setCage(cage);
                result.setTotalFemale(0);
                result.setTotalMale(0);
                result.setTotalPet(0);
                result.setPetLive(0);
                result.setPetDie(0);
//                result.setPetUilness(0);
//                result.setPetNomal(0);
                result.setStatisticPetByAgeList(createStatisticByAge());
                result.setStatisticPetByWeightList(createStatisticByWeight());
                result.setStatisticStatusUilnessPetList(createStatisticStatusUilnessPet());

                getPetList(cage, petList).forEach(pet -> {
                    result.setTotalPet(result.getTotalPet() + 1);
                    if (pet.getSex() == 1){
                        result.setTotalMale(result.getTotalMale() + 1);
                    }else {
                        result.setTotalFemale(result.getTotalFemale() + 1);
                    }
                    if (pet.getStatus() == -1){
                        result.setPetDie(result.getPetDie()+1);
                    }else {
                        result.setPetLive(result.getPetLive()+1);
                    }
//                    if (pet.getUilness() != null || !pet.getUilness().equals("")){
//                        result.setPetUilness(result.getPetUilness()+1);
//                    }else {
//                        result.setPetNomal(result.getPetNomal()+1);
//                    }
                    result.getStatisticPetByAgeList().forEach(age -> {
                        if (pet.getAge() >= age.getFrom() && pet.getAge() < age.getTo()){
                            age.setQuantity(age.getQuantity() + 1);
                        }
                    });
                    result.getStatisticPetByWeightList().forEach(weight -> {
                        if (pet.getWeight() >= weight.getFrom() && pet.getWeight() < weight.getTo()){
                            weight.setQuantity(weight.getQuantity() + 1);
                        }
                    });
                    if (!uilnessList.isEmpty() && pet.getStatus() != -1){
                        if (pet.getUilness() == null ){
                            result.getStatisticStatusUilnessPetList().get(0).setTotalPet(result.getStatisticStatusUilnessPetList().get(0).getTotalPet() + 1);
                            if (pet.getSex() == 1){
                                result.getStatisticStatusUilnessPetList().get(0).setTotalMale(result.getStatisticStatusUilnessPetList().get(0).getTotalMale() + 1);
                            }else {
                                result.getStatisticStatusUilnessPetList().get(0).setTotalFemale(result.getStatisticStatusUilnessPetList().get(0).getTotalFemale() + 1);
                            }
                        }else {
                            String[]  uilnesses = pet.getUilness().split(",");
                            AtomicInteger uilnessCheckPoint = new AtomicInteger(0);
                            Arrays.stream(uilnesses).forEach(uilnessCode -> {
                                uilnessList.forEach(uilness -> {
                                    if (uilnessCode.contains(uilness.getCode())){
                                        if (uilness.getScore() >= uilnessCheckPoint.get()){
                                            uilnessCheckPoint.set(uilness.getScore());
                                        }
                                    }
                                });
                            });
                            result.getStatisticStatusUilnessPetList().get(uilnessCheckPoint.get()).setTotalPet(result.getStatisticStatusUilnessPetList().get(uilnessCheckPoint.get()).getTotalPet() + 1);
                            if (pet.getSex() == 1){
                                result.getStatisticStatusUilnessPetList().get(uilnessCheckPoint.get()).setTotalMale(result.getStatisticStatusUilnessPetList().get(uilnessCheckPoint.get()).getTotalMale() + 1);
                            }else {
                                result.getStatisticStatusUilnessPetList().get(uilnessCheckPoint.get()).setTotalFemale(result.getStatisticStatusUilnessPetList().get(uilnessCheckPoint.get()).getTotalFemale() + 1);
                            }
                        }
                    }
                });
                results.add(result);
            });
        Page<ResultStatisticByCageRes> res = new PageImpl<>(results, cagePage.getPageable(), cagePage.getTotalElements());
        return new BaseResponse().success(res);
    }

    public List<Cage> getCageList(Farm farm, List<Cage> cageList){
        List<Cage> results = cageList.stream().filter(e -> e.getFarm().equals(farm) && e.getStatus() == 1).collect(Collectors.toList());
        return results;
    }

    public List<Pet> getPetList(Cage cage, List<Pet> petList){
        List<Pet> results = petList.stream().filter(e -> e.getCage().equals(cage) && e.getStatus() == 1).collect(Collectors.toList());
        return results;
    }
}
