package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.response.*;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    FarmService farmService;

    @Autowired
    PetTypeService petTypeService;

    @Autowired
    UilnessService uilnessService;
    @Override
    public List<StatisticTotalPetFollowFarmRes> statisticTotalPetFollowFarm() {
//        List<Farm> allData = farmService.findByFilter();
//        List<StatisticTotalPetFollowFarmRes> resLists = allData.stream().map(e -> {
//            StatisticTotalPetFollowFarmRes result = new StatisticTotalPetFollowFarmRes();
//            result.setFarmId(e.getId());
//            result.setFarmName(e.getName());
//            result.setFarmCode(e.getCode());
//            result.setTotalPet((long) getPetFollow(e));
//            statisticPet(result, e);
//            return result;
//        }).collect(Collectors.toList());
//        return resLists;
    return null;}

//    public int getPetFollow(Farm farm){
//        return farm.getCageList().stream().map(e -> e.getPetList().size()).reduce(0, Integer::sum);
//    }
//
//    public void statisticPet(StatisticTotalPetFollowFarmRes result, Farm farm){
//        List<StatisticPetType> statisticPetTypeList = petTypeService.getByActive() != null?petTypeService.getAll().stream().map(e -> {
//                StatisticPetType statisticPetType = new StatisticPetType();
//                statisticPetType.setType(e.getId());
//                statisticPetType.setName(e.getName());
//                statisticPetType.setQuantity(0);
//                return statisticPetType;
//        }).collect(Collectors.toList()) : new ArrayList<>();
//
//        List<StatisticPetUilness> statisiticPetUilnessList = uilnessService.getByActive() != null ? uilnessService.getAll().stream().map(e -> {
//                StatisticPetUilness statisticPetUilness = new StatisticPetUilness();
//                statisticPetUilness.setUilness(e.getId());
//                statisticPetUilness.setName(e.getName());
//                statisticPetUilness.setQuantity(0);
//                statisticPetUilness.setScore(e.getUilnessType().getScore());
//                return statisticPetUilness;
//        }).collect(Collectors.toList()) : new ArrayList<>();
//
//        farm.getCageList().stream().map(e -> {
//            return e.getPetList().stream().map(i -> {
//                if(i.getStatus()==1){
//                    statisticAgePet(i, result);
//                    statisticWeightPet(i, result);
//                    statisticSexPet(i, result);
//                    statisticRebornPet(i, result);
//                    statisticPetType(i, result, statisticPetTypeList);
//                    statisticPetUilness(i, result, statisiticPetUilnessList);
//                }
//                return null;
//            }).collect(Collectors.toList());
//        }).collect(Collectors.toList());
//        result.setStatisticPetTypeList(statisticPetTypeList);
//        result.setStatisiticPetUilnessList(statisiticPetUilnessList);
//    }
//
//    public void statisticWeightPet(Pet i, StatisticTotalPetFollowFarmRes result){
//        StatisitcPetFollowWeight statisitcPetFollowWeight  = new StatisitcPetFollowWeight();
//                if(i.getWeight() >= statisitcPetFollowWeight.getMusty1().getFrom() && i.getWeight() <= statisitcPetFollowWeight.getMusty1().getTo())
//                    statisitcPetFollowWeight.getMusty1().setQuantity(statisitcPetFollowWeight.getMusty1().getQuantity() + 1);
//
//                if(i.getWeight() > statisitcPetFollowWeight.getMusty2().getFrom() && i.getWeight() <= statisitcPetFollowWeight.getMusty2().getTo())
//                    statisitcPetFollowWeight.getMusty2().setQuantity(statisitcPetFollowWeight.getMusty2().getQuantity() + 1);
//
//                if(i.getWeight() > statisitcPetFollowWeight.getMusty3().getFrom() && i.getWeight() <= statisitcPetFollowWeight.getMusty3().getTo())
//                    statisitcPetFollowWeight.getMusty3().setQuantity(statisitcPetFollowWeight.getMusty3().getQuantity() + 1);
//
//                if(i.getWeight() > statisitcPetFollowWeight.getMusty4().getFrom() && i.getWeight() <= statisitcPetFollowWeight.getMusty4().getTo())
//                    statisitcPetFollowWeight.getMusty4().setQuantity(statisitcPetFollowWeight.getMusty4().getQuantity() + 1);
//
//                if(i.getWeight() > statisitcPetFollowWeight.getMusty5().getFrom())
//                    statisitcPetFollowWeight.getMusty5().setQuantity(statisitcPetFollowWeight.getMusty5().getQuantity() + 1);
//
//        result.setStatisitcPetFollowWeight(statisitcPetFollowWeight);
//    }
//
//    public void statisticAgePet(Pet i, StatisticTotalPetFollowFarmRes result){
//        StatisticPetFollowAge statisticPetFollowAge  = new StatisticPetFollowAge();
//        if(i.getAge() > statisticPetFollowAge.getMusty1().getFrom() && i.getAge() <= statisticPetFollowAge.getMusty1().getTo())
//            statisticPetFollowAge.getMusty1().setQuantity(statisticPetFollowAge.getMusty1().getQuantity() + 1);
//
//        if(i.getAge() > statisticPetFollowAge.getMusty2().getFrom() && i.getAge() <= statisticPetFollowAge.getMusty2().getTo())
//            statisticPetFollowAge.getMusty2().setQuantity(statisticPetFollowAge.getMusty2().getQuantity() + 1);
//
//        if(i.getAge() > statisticPetFollowAge.getMusty3().getFrom() && i.getAge() <= statisticPetFollowAge.getMusty3().getTo())
//            statisticPetFollowAge.getMusty3().setQuantity(statisticPetFollowAge.getMusty3().getQuantity() + 1);
//
//        if(i.getAge() > statisticPetFollowAge.getMusty4().getFrom() && i.getAge() <= statisticPetFollowAge.getMusty4().getTo())
//            statisticPetFollowAge.getMusty4().setQuantity(statisticPetFollowAge.getMusty4().getQuantity() + 1);
//
//        if(i.getAge() > statisticPetFollowAge.getMusty5().getFrom() && i.getAge() <= statisticPetFollowAge.getMusty5().getTo())
//            statisticPetFollowAge.getMusty5().setQuantity(statisticPetFollowAge.getMusty5().getQuantity() + 1);
//
//        if(i.getAge() > statisticPetFollowAge.getMusty6().getFrom() && i.getAge() <= statisticPetFollowAge.getMusty6().getTo())
//            statisticPetFollowAge.getMusty6().setQuantity(statisticPetFollowAge.getMusty6().getQuantity() + 1);
//
//        if(i.getAge() > statisticPetFollowAge.getMusty1().getFrom() && i.getAge() <= statisticPetFollowAge.getMusty1().getTo())
//            statisticPetFollowAge.getMusty7().setQuantity(statisticPetFollowAge.getMusty7().getQuantity() + 1);
//
//        if(i.getAge() > statisticPetFollowAge.getMusty8().getFrom() && i.getAge() <= statisticPetFollowAge.getMusty8().getTo())
//            statisticPetFollowAge.getMusty8().setQuantity(statisticPetFollowAge.getMusty8().getQuantity() + 1);
//
//        if(i.getAge() > statisticPetFollowAge.getMusty9().getFrom() && i.getAge() <= statisticPetFollowAge.getMusty9().getTo())
//            statisticPetFollowAge.getMusty9().setQuantity(statisticPetFollowAge.getMusty9().getQuantity() + 1);
//
//        if(i.getAge() > statisticPetFollowAge.getMusty10().getFrom())
//            statisticPetFollowAge.getMusty10().setQuantity(statisticPetFollowAge.getMusty10().getQuantity() + 1);
//
//        result.setStatisticPetFollowAge(statisticPetFollowAge);
//
//    }
//
//    public void statisticSexPet(Pet i, StatisticTotalPetFollowFarmRes result){
//        if (i.getSex() == 0)
//            result.setQuantityMale(result.getQuantityMale() + 1);
//        if (i.getSex() == 1)
//            result.setQuantityFemail(result.getQuantityFemail() + 1);
//    }
//
//    public void statisticRebornPet(Pet i, StatisticTotalPetFollowFarmRes result){
//        if (i.getParent() == null){
//            result.setQuantityRebornOutFarm(result.getQuantityRebornOutFarm() + 1);
//        }else {
//            result.setQuantityRebornInFarm(result.getQuantityRebornInFarm() + 1);
//        }
//    }
//
//    public void statisticPetType(Pet i, StatisticTotalPetFollowFarmRes result, List<StatisticPetType> statisticPetTypeList){
//        statisticPetTypeList.stream().map(e -> {
//            if (e.getType().equals(i.getType().getId())){
//                e.setQuantity(e.getQuantity() + 1);
//            }
//            return null;
//        }).collect(Collectors.toList());
//    }
//
//    public void statisticPetUilness(Pet i, StatisticTotalPetFollowFarmRes result, List<StatisticPetUilness> statisticPetUilnessList){
//        statisticPetUilnessList.stream().forEach(e -> {
//            if (e.getUilness().equals(i.getUilness().getId())){
//                e.setQuantity(e.getQuantity() + 1);
//            }
//        });
//    }

}
