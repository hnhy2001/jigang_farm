package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.PetRepository;
import com.example.jingangfarmmanagement.repository.dto.PetStatisticDto;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.PetStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class PetStatisticImpl {
    @Autowired
    PetRepository petRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> filterPetCommon(Long startDate, Long endDate, List<Integer> sex, List<Integer> status, List<Long> cageId, List<Long> farmId, Integer age) {
        StringBuilder jpql = new StringBuilder("SELECT DATE(p.createDate) as createDate,p.name as name, COUNT(p) as count " +
                "FROM Pet p " +
                "JOIN p.cage c " +
                "JOIN c.farm f " +
                "WHERE p.createDate BETWEEN :startDate AND :endDate ");

        if (sex != null && !sex.isEmpty()) {
            jpql.append("AND p.sex IN :sex ");
        }
        if (status != null && !status.isEmpty()) {
            jpql.append("AND p.status IN :status ");
        }
        if (cageId != null && !cageId.isEmpty()) {
            jpql.append("AND p.cage.id IN :cageId ");
        }
        if (farmId != null && !farmId.isEmpty()) {
            jpql.append("AND c.farm.id IN :farmId ");
        }
        jpql.append("GROUP BY DATE(p.createDate),name " +
                "ORDER BY DATE(p.createDate)");

        TypedQuery<Object[]> query = entityManager.createQuery(jpql.toString(), Object[].class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        if (sex != null && !sex.isEmpty()) {
            query.setParameter("sex", sex);
        }
        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }
        if (cageId != null && !cageId.isEmpty()) {
            query.setParameter("cageId", cageId);
        }
        if (farmId != null && !farmId.isEmpty()) {
            query.setParameter("farmId", farmId);
        }


        List<Object[]> results = query.getResultList();
        List<Object[]> filteredResults = new ArrayList<>();

//        if (age != 0) {
//            filteredResults = results.stream()
//                    .filter(result -> {
//                        // Giả sử rằng tên con vật được lưu trữ trong một thuộc tính `name` của `Pet`
//                        String name = result[0].toString();
//                        String yearStr = name.substring(0, 2);
//                        String monthStr = name.substring(2, 4);
//                        int birthYear = Integer.parseInt("20" + yearStr);
//                        int birthMonth = Integer.parseInt(monthStr);
//
//                        // Tính số tháng tuổi
//                        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, 1);
//                        LocalDate currentDate = LocalDate.now();
//                        int ageInMonths = (int) Period.between(birthDate, currentDate).toTotalMonths();
//
//                        // Lọc theo tuổi
//                        switch (age) {
//                            case 1:
//                                return ageInMonths <= 12;
//                            case 2:
//                                return ageInMonths > 12 && ageInMonths <= 19;
//                            case 3:
//                                return ageInMonths >= 24 && ageInMonths <= 48;
//                            case 4:
//                                return ageInMonths > 48;
//                            default:
//                                return false;
//                        }
//                    })
//                    .collect(Collectors.toList());
//        }

        return results;
    }

    public PetStatisticDto petStatistic(Long startDate, Long endDate, List<Integer> sex, List<Integer> status, List<Long> cageId, List<Long> farmId, Integer age) {
        List<Object[]> resultMales = filterPetCommon(startDate, endDate, List.of(0), status, cageId, farmId, age);
        List<Object[]> resultFeMales = filterPetCommon(startDate, endDate, List.of(1), status, cageId, farmId, age);
        long totalCountMale = resultMales.stream()
                .mapToLong(result -> (Long) result[2])
                .sum();
        long totalCountFeMale = resultFeMales.stream()
                .mapToLong(result -> (Long) result[2])
                .sum();
        return new PetStatisticDto(totalCountMale,totalCountFeMale,0L);
    }
//    public List<Long> getExportPetIds(Long startDate, Long endDate) {
//        String jpql = "SELECT ep.petId " +
//                "FROM ExportPet ep " +
//                "WHERE ep.date BETWEEN :startDate AND :endDate";
//
//        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
//        query.setParameter("startDate", startDate);
//        query.setParameter("endDate", endDate);
//
//        return query.getResultList();
//    }

    public List<Object[]> filterPetDeath(Long startDate, Long endDate, List<Integer> sex, List<Integer> status, List<Long> cageId, List<Long> farmId, Integer age) {
        StringBuilder jpql = new StringBuilder("SELECT p.sex, DATE(p.createDate) as createDate, COUNT(p) as count " +
                "FROM Pet p " +
                "JOIN p.cage c " +
                "JOIN c.farm f " +
                "JOIN ExportPet ep on p.id = ep.petId " +
                "WHERE p.createDate BETWEEN :startDate AND :endDate ");

        if (sex != null && !sex.isEmpty()) {
            jpql.append("AND p.sex IN :sex ");
        }
        if (status != null && !status.isEmpty()) {
            jpql.append("AND p.status IN :status ");
        }
        if (cageId != null && !cageId.isEmpty()) {
            jpql.append("AND p.cage.id IN :cageId ");
        }
        if (farmId != null && !farmId.isEmpty()) {
            jpql.append("AND c.farm.id IN :farmId ");
        }
        jpql.append("GROUP BY p.sex, DATE(p.createDate) " +
                "ORDER BY DATE(p.createDate), p.sex");

        TypedQuery<Object[]> query = entityManager.createQuery(jpql.toString(), Object[].class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        if (sex != null && !sex.isEmpty()) {
            query.setParameter("sex", sex);
        }
        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }
        if (cageId != null && !cageId.isEmpty()) {
            query.setParameter("cageId", cageId);
        }
        if (farmId != null && !farmId.isEmpty()) {
            query.setParameter("farmId", farmId);
        }

        List<Object[]> results = query.getResultList();
        List<Object[]> filteredResults = new ArrayList<>();

        if (age != 0) {
            filteredResults = results.stream()
                    .filter(result -> {
                        LocalDate createDate = (LocalDate) result[1];
                        Long count = (Long) result[2];

                        // Giả sử rằng tên con vật được lưu trữ trong một thuộc tính `name` của `Pet`
                        Pet pet = (Pet)result[0];
                        String name = pet.getName();
                        String yearStr = name.substring(0, 2);
                        String monthStr = name.substring(2, 4);
                        int birthYear = Integer.parseInt("20" + yearStr);
                        int birthMonth = Integer.parseInt(monthStr);

                        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, 1);
                        LocalDate currentDate = LocalDate.now();
                        int ageInMonths = (int) Period.between(birthDate, currentDate).toTotalMonths();

                        switch (age) {
                            case 1:
                                return ageInMonths <= 12;
                            case 2:
                                return ageInMonths > 12 && ageInMonths <= 19;
                            case 3:
                                return ageInMonths >= 24 && ageInMonths <= 48;
                            case 4:
                                return ageInMonths > 48;
                            default:
                                return false;
                        }
                    })
                    .collect(Collectors.toList());
        }

        return filteredResults;
    }



}
