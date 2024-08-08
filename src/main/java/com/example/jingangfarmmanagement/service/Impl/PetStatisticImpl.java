package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.PetRepository;
import com.example.jingangfarmmanagement.repository.dto.PetDeathStatisticDto;
import com.example.jingangfarmmanagement.repository.dto.PetStatisticDto;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.PetStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class PetStatisticImpl {
    @Autowired
    PetRepository petRepository;
    @PersistenceContext
    private EntityManager entityManager;
    public List<Object[]> filterPetCommon(Long startDate, Long endDate, List<Integer> sex, List<Integer> status, List<Long> cageId, List<Long> farmId, Integer age) {
        StringBuilder jpql = new StringBuilder("SELECT DATE(p.createDate) as createDate, p.name as name, COUNT(p) as count " +
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
        jpql.append("GROUP BY DATE(p.createDate), p.name " +
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

        if (age != null) {
            results = results.stream()
                    .filter(result -> {
                        LocalDate createDate = ((Date) result[0]).toLocalDate();
                        String petName = (String) result[1];
                        String yearStr = petName.substring(0, 2);
                        String monthStr = petName.substring(2, 4);
                        int birthYear = Integer.parseInt("20" + yearStr);
                        int birthMonth = Integer.parseInt(monthStr);

                        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, 1);
                        LocalDate currentDate = LocalDate.now();
                        int ageInMonths = (int) Period.between(birthDate, currentDate).toTotalMonths();

                        switch (age) {
                            case 1:
                                return ageInMonths <= 12;
                            case 2:
                                return ageInMonths > 12 && ageInMonths <= 24;
                            case 3:
                                return ageInMonths > 24 && ageInMonths <= 48;
                            case 4:
                                return ageInMonths > 48;
                            default:
                                return false;
                        }
                    })
                    .collect(Collectors.toList());
        }

        return results;
    }

//    public List<Object[]> filterPetCommon(Long startDate, Long endDate, List<Integer> sex, List<Integer> status, List<Long> cageId, List<Long> farmId, Integer age) {
//        StringBuilder jpql = new StringBuilder("SELECT DATE(p.createDate) as createDate,p.name as name, COUNT(p) as count " +
//                "FROM Pet p " +
//                "JOIN p.cage c " +
//                "JOIN c.farm f " +
//                "WHERE p.createDate BETWEEN :startDate AND :endDate ");
//
//        if (sex != null && !sex.isEmpty()) {
//            jpql.append("AND p.sex IN :sex ");
//        }
//        if (status != null && !status.isEmpty()) {
//            jpql.append("AND p.status IN :status ");
//        }
//        if (cageId != null && !cageId.isEmpty()) {
//            jpql.append("AND p.cage.id IN :cageId ");
//        }
//        if (farmId != null && !farmId.isEmpty()) {
//            jpql.append("AND c.farm.id IN :farmId ");
//        }
//        jpql.append("GROUP BY DATE(p.createDate),name " +
//                "ORDER BY DATE(p.createDate)");
//
//        TypedQuery<Object[]> query = entityManager.createQuery(jpql.toString(), Object[].class);
//        query.setParameter("startDate", startDate);
//        query.setParameter("endDate", endDate);
//
//        if (sex != null && !sex.isEmpty()) {
//            query.setParameter("sex", sex);
//        }
//        if (status != null && !status.isEmpty()) {
//            query.setParameter("status", status);
//        }
//        if (cageId != null && !cageId.isEmpty()) {
//            query.setParameter("cageId", cageId);
//        }
//        if (farmId != null && !farmId.isEmpty()) {
//            query.setParameter("farmId", farmId);
//        }
//
//
//        List<Object[]> results = query.getResultList();
//
//        if (age != null) {
//            results = results.stream()
//                    .filter(result -> {
//                        LocalDate createDate = (LocalDate) result[1];
//                        String petName = (String) result[0];
//                        String yearStr = petName.substring(0, 2);
//                        String monthStr = petName.substring(2, 4);
//                        int birthYear = Integer.parseInt("20" + yearStr);
//                        int birthMonth = Integer.parseInt(monthStr);
//
//                        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, 1);
//                        LocalDate currentDate = LocalDate.now();
//                        int ageInMonths = (int) Period.between(birthDate, currentDate).toTotalMonths();
//
//                        switch (age) {
//                            case 1:
//                                return ageInMonths <= 12;
//                            case 2:
//                                return ageInMonths > 12 && ageInMonths <= 24;
//                            case 3:
//                                return ageInMonths > 24 && ageInMonths <= 48;
//                            case 4:
//                                return ageInMonths > 48;
//                            default:
//                                return false;
//                        }
//                    })
//                    .collect(Collectors.toList());
//        }
//
//        return results;
//    }

    public PetStatisticDto petStatistic(Long startDate, Long endDate, List<Integer> sex, List<Integer> status, List<Long> cageId, List<Long> farmId, Integer age) {
        List<Object[]> resultMales = filterPetCommon(startDate, endDate, List.of(1), status, cageId, farmId, age);
        List<Object[]> resultFeMales = filterPetCommon(startDate, endDate, List.of(0), status, cageId, farmId, age);
        List<Object[]> resultNaMales = filterPetCommon(startDate, endDate, List.of(2), status, cageId, farmId, age);

        long totalCountMale = 0L;
        long totalCountFeMale = 0L;
        long totalCountNa = 0L;

        if (sex != null && !sex.isEmpty()) {
            if (sex.contains(1)) {
                totalCountMale = resultMales.stream()
                        .mapToLong(result -> ((Number) result[2]).longValue())
                        .sum();
            }
            if (sex.contains(0)) {
                totalCountFeMale = resultFeMales.stream()
                        .mapToLong(result -> ((Number) result[2]).longValue())
                        .sum();
            }
            if (sex.contains(2)) {
                totalCountNa = resultNaMales.stream()
                        .mapToLong(result -> ((Number) result[2]).longValue())
                        .sum();
            }
        }

        return new PetStatisticDto(totalCountMale, totalCountFeMale, totalCountNa);
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
//public List<PetDeathStatisticDto> getPetDeathPerDay(Long startDate, Long endDate, List<Integer> sex, List<Integer> status, List<Long> cageId, List<Long> farmId, Integer age) {
//    // Tạo câu truy vấn JPQL
//    StringBuilder jpql = new StringBuilder("SELECT p.sex as sex, DATE(p.createDate) as createDate, COUNT(p) as quantity " +
//            "FROM Pet p " +
//            "JOIN p.cage c " +
//            "JOIN c.farm f " +
//            "JOIN ExportPet ep on p.id = ep.petId " +
//            "WHERE p.createDate BETWEEN :startDate AND :endDate ");
//
//    if (sex != null && !sex.isEmpty()) {
//        jpql.append("AND p.sex IN :sex ");
//    }
//    if (status != null && !status.isEmpty()) {
//        jpql.append("AND p.status IN :status ");
//    }
//    if (cageId != null && !cageId.isEmpty()) {
//        jpql.append("AND p.cage.id IN :cageId ");
//    }
//    if (farmId != null && !farmId.isEmpty()) {
//        jpql.append("AND c.farm.id IN :farmId ");
//    }
//    jpql.append("GROUP BY p.sex, DATE(p.createDate) " +
//            "ORDER BY DATE(p.createDate), p.sex");
//
//    TypedQuery<Object[]> query = entityManager.createQuery(jpql.toString(), Object[].class);
//    query.setParameter("startDate", startDate);
//    query.setParameter("endDate", endDate);
//
//    if (sex != null && !sex.isEmpty()) {
//        query.setParameter("sex", sex);
//    }
//    if (status != null && !status.isEmpty()) {
//        query.setParameter("status", status);
//    }
//    if (cageId != null && !cageId.isEmpty()) {
//        query.setParameter("cageId", cageId);
//    }
//    if (farmId != null && !farmId.isEmpty()) {
//        query.setParameter("farmId", farmId);
//    }
//
//    List<Object[]> results = query.getResultList();
//
//    // Nếu age được cung cấp, lọc kết quả theo tuổi
//    if (age != null) {
//        results = results.stream()
//                .filter(result -> {
//                    LocalDate createDate = ((java.sql.Date) result[1]).toLocalDate();
//                    String petName = (String) result[0]; // Cần điều chỉnh nếu cấu trúc dữ liệu khác
//                    String yearStr = petName.substring(0, 2);
//                    String monthStr = petName.substring(2, 4);
//                    int birthYear = Integer.parseInt("20" + yearStr);
//                    int birthMonth = Integer.parseInt(monthStr);
//
//                    LocalDate birthDate = LocalDate.of(birthYear, birthMonth, 1);
//                    LocalDate currentDate = LocalDate.now();
//                    int ageInMonths = (int) Period.between(birthDate, currentDate).toTotalMonths();
//
//                    switch (age) {
//                        case 1:
//                            return ageInMonths <= 12;
//                        case 2:
//                            return ageInMonths > 12 && ageInMonths <= 24;
//                        case 3:
//                            return ageInMonths > 24 && ageInMonths <= 48;
//                        case 4:
//                            return ageInMonths > 48;
//                        default:
//                            return false;
//                    }
//                })
//                .collect(Collectors.toList());
//    }
//
//    // Tạo danh sách tất cả các ngày trong khoảng thời gian tìm kiếm
//    LocalDate start = new java.sql.Date(startDate).toLocalDate();
//    LocalDate end = new java.sql.Date(endDate).toLocalDate();
//    List<LocalDate> allDates = new ArrayList<>();
//    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
//        allDates.add(date);
//    }
//
//    // Tạo bản đồ lưu trữ số liệu thống kê theo ngày
//    Map<LocalDate, PetDeathStatisticDto> dailyStats = new LinkedHashMap<>();
//
//    // Khởi tạo tất cả các ngày trong bản đồ với giá trị 0
//    for (LocalDate date : allDates) {
//        PetDeathStatisticDto defaultStat = new PetDeathStatisticDto(date.toString(), 0L, 0L, 0L);
//        dailyStats.put(date, defaultStat);
//    }
//
//    // Cập nhật số liệu từ kết quả truy vấn
//    for (Object[] result : results) {
//        int sexValue = (int) result[0];
//        LocalDate date = ((java.sql.Date) result[1]).toLocalDate();
//        Long quantity = (Long) result[2];
//
//        PetDeathStatisticDto stat = dailyStats.get(date);
//        if (stat != null) {
//            if (sexValue == 1) {
//                stat.setMaleDeaths(stat.getMaleDeaths() + quantity);
//            } else if (sexValue == 0) {
//                stat.setFemaleDeaths(stat.getFemaleDeaths() + quantity);
//            } else {
//                stat.setNaDeaths(stat.getNaDeaths() + quantity);
//            }
//        }
//    }
//
//    return new ArrayList<>(dailyStats.values());
//}
    public List<PetDeathStatisticDto> getPetDeathPerDay(Long startDate, Long endDate, List<Integer> sex, List<Integer> status, List<Long> cageId, List<Long> farmId, Integer age) {
        StringBuilder jpql = new StringBuilder("SELECT p.sex as sex, DATE(ep.exportDate) as exportDate, COUNT(p) as quantity " +
                "FROM Pet p " +
                "JOIN p.cage c " +
                "JOIN c.farm f " +
                "JOIN ExportPet ep on p.id = ep.petId " +
                "WHERE ep.exportDate BETWEEN :startDate AND :endDate ");

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
        jpql.append("GROUP BY p.sex, DATE(ep.exportDate) " +
                "ORDER BY DATE(ep.exportDate), p.sex");

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

        if (age != null) {
            results = results.stream()
                    .filter(result -> {
                        LocalDate createDate = ((Date) result[1]).toLocalDate();
                        // Assuming the name of the pet is in the result and parsing the birth date from it
                        String petName = (String) result[0]; // Adjust this according to your actual data structure
                        String yearStr = petName.substring(0, 2);
                        String monthStr = petName.substring(2, 4);
                        int birthYear = Integer.parseInt("20" + yearStr);
                        int birthMonth = Integer.parseInt(monthStr);

                        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, 1);
                        LocalDate currentDate = LocalDate.now();
                        int ageInMonths = (int) Period.between(birthDate, currentDate).toTotalMonths();

                        switch (age) {
                            case 1:
                                return ageInMonths <= 12;
                            case 2:
                                return ageInMonths > 12 && ageInMonths <= 24;
                            case 3:
                                return ageInMonths > 24 && ageInMonths <= 48;
                            case 4:
                                return ageInMonths > 48;
                            default:
                                return false;
                        }
                    })
                    .collect(Collectors.toList());
        }

        Map<LocalDate, PetDeathStatisticDto> dailyStats = new LinkedHashMap<>();

        for (Object[] result : results) {
            int sexValue = (int) result[0];
            LocalDate date = ((Date) result[1]).toLocalDate();
            Long quantity = (Long) result[2];
            String formattedDate = date.toString();

            PetDeathStatisticDto stat = dailyStats.getOrDefault(date, new PetDeathStatisticDto(formattedDate, 0L, 0L,0L));
            if (sexValue == 1) {
                stat.setMaleDeaths(stat.getMaleDeaths() + quantity);
            } else if (sexValue == 0) {
                stat.setFemaleDeaths(stat.getFemaleDeaths() + quantity);
            }
            else if(sexValue==2){
                stat.setNaDeaths(stat.getNaDeaths() + quantity);
            }
            dailyStats.put(date, stat);
        }

        return new ArrayList<>(dailyStats.values());
    }
    public List<PetDeathStatisticDto> getPetBornPerDay(Long startDate, Long endDate, List<Integer> sex, List<Integer> status, List<Long> cageId, List<Long> farmId, Integer age) {
        StringBuilder jpql = new StringBuilder("SELECT p.sex as sex, DATE(p.birthNumber) as birthNumber, COUNT(p) as quantity " +
                "FROM Pet p " +
                "JOIN p.cage c " +
                "JOIN c.farm f " +
                "WHERE p.birthNumber BETWEEN :startDate AND :endDate ");

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
        jpql.append("GROUP BY p.sex, DATE(p.birthNumber) " +
                "ORDER BY DATE(p.birthNumber), p.sex");

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

        if (age != null) {
            results = results.stream()
                    .filter(result -> {
                        LocalDate createDate = ((Date) result[1]).toLocalDate();
                        // Assuming the name of the pet is in the result and parsing the birth date from it
                        String petName = (String) result[0]; // Adjust this according to your actual data structure
                        String yearStr = petName.substring(0, 2);
                        String monthStr = petName.substring(2, 4);
                        int birthYear = Integer.parseInt("20" + yearStr);
                        int birthMonth = Integer.parseInt(monthStr);

                        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, 1);
                        LocalDate currentDate = LocalDate.now();
                        int ageInMonths = (int) Period.between(birthDate, currentDate).toTotalMonths();

                        switch (age) {
                            case 1:
                                return ageInMonths <= 12;
                            case 2:
                                return ageInMonths > 12 && ageInMonths <= 24;
                            case 3:
                                return ageInMonths > 24 && ageInMonths <= 48;
                            case 4:
                                return ageInMonths > 48;
                            default:
                                return false;
                        }
                    })
                    .collect(Collectors.toList());
        }

        Map<LocalDate, PetDeathStatisticDto> dailyStats = new LinkedHashMap<>();

        for (Object[] result : results) {
            int sexValue = (int) result[0];
            LocalDate date = ((Date) result[1]).toLocalDate();
            Long quantity = (Long) result[2];
            String formattedDate = date.toString();

            PetDeathStatisticDto stat = dailyStats.getOrDefault(date, new PetDeathStatisticDto(formattedDate, 0L, 0L,0L));
            if (sexValue == 1) {
                stat.setMaleDeaths(stat.getMaleDeaths() + quantity);
            } else if (sexValue == 0) {
                stat.setFemaleDeaths(stat.getFemaleDeaths() + quantity);
            }
            else if(sexValue==2) {
                stat.setNaDeaths(stat.getNaDeaths() + quantity);
            }
            dailyStats.put(date, stat);
        }

        return new ArrayList<>(dailyStats.values());
    }
    @Transactional
    public void syncDateOfBirth() {
        // Lấy tất cả các đối tượng Pet từ cơ sở dữ liệu
        List<Pet> pets = petRepository.findAll();

        // Cập nhật ngày sinh cho từng đối tượng Pet
        pets.forEach(pet -> {
            Long dateOfBirth = calculateBirthDate(pet.getName())!=null ? Long.valueOf(calculateBirthDate(pet.getName())):0L;
            if (dateOfBirth != -1) {
                pet.setBirthNumber(dateOfBirth);
            } else {
                pet.setBirthNumber(null); // Hoặc không thực hiện gì nếu không hợp lệ
            }
        });

        // Lưu tất cả các đối tượng Pet đã cập nhật
        petRepository.saveAll(pets);
    }


    public  String calculateBirthDate(String dateStr) {
        // Xử lý định dạng yymmxxx
        if (dateStr != null && dateStr.matches("\\d{6}\\d{1,3}")) {
            return formatDate(calculateDateFromYymmxxx(dateStr));
        }

        // Xử lý định dạng yym-x-xx-cnx
        if (dateStr != null && dateStr.matches("\\d{2}-\\d-\\d{2}-\\w+")) {
            return formatDate(calculateDateFromYymXxxCnx(dateStr));
        }

        // Nếu định dạng không hợp lệ
        return null;
    }

    /**
     * Tạo ngày sinh từ chuỗi định dạng yymmxxx.
     * @param yymmxxx Chuỗi định dạng yymmxxx (ví dụ: "0803058").
     * @return Đối tượng LocalDateTime đại diện cho ngày sinh hoặc null nếu ngày sinh không hợp lệ.
     */
    private static LocalDateTime calculateDateFromYymmxxx(String yymmxxx) {
        if (yymmxxx.length() != 7) {
            return null;
        }

        String yy = yymmxxx.substring(0, 2);
        String mm = yymmxxx.substring(2, 4);
        String xxx = yymmxxx.substring(4);

        String currentYearPrefix = String.valueOf(LocalDate.now().getYear()).substring(0, 2);
        String yyyy;

            yyyy = "20" + yy;


        int day;
        try {
            day = Integer.parseInt(xxx.substring(0, 2));
        } catch (NumberFormatException e) {
            return null;
        }

        LocalDate birthDate;
        try {
            birthDate = LocalDate.of(Integer.parseInt(yyyy), Integer.parseInt(mm), day);
        } catch (Exception e) {
            return null;
        }

        return birthDate.atStartOfDay(); // Cung cấp ngày sinh lúc 00:00:00
    }

    /**
     * Tạo ngày sinh từ chuỗi định dạng yym-x-xx-cnx.
     * @param yymXxxCnx Chuỗi định dạng yym-x-xx-cnx (ví dụ: "080-5-15-CN6").
     * @return Đối tượng LocalDateTime đại diện cho ngày sinh hoặc null nếu ngày sinh không hợp lệ.
     */
    private static LocalDateTime calculateDateFromYymXxxCnx(String yymXxxCnx) {
        String[] parts = yymXxxCnx.split("-");
        if (parts.length < 3) {
            return null;
        }

        String yy = parts[0].substring(0, 2);
        String mm = parts[0].substring(2, 4);;
        String dayStr = parts[2];

        String currentYearPrefix = String.valueOf(LocalDate.now().getYear()).substring(0, 2);
        String yyyy;

            yyyy = "20" + yy;

        int day;
        try {
            day = Integer.parseInt(dayStr);
        } catch (NumberFormatException e) {
            return null;
        }

        LocalDate birthDate;
        try {
            birthDate = LocalDate.of(Integer.parseInt(yyyy), Integer.parseInt(mm), day);
        } catch (Exception e) {
            return null;
        }

        return birthDate.atStartOfDay(); // Cung cấp ngày sinh lúc 00:00:00
    }

    /**
     * Chuyển đổi đối tượng LocalDateTime thành chuỗi định dạng yyyymmddhhmmss.
     * @param dateTime Đối tượng LocalDateTime cần chuyển đổi.
     * @return Chuỗi định dạng yyyymmddhhmmss hoặc null nếu đối tượng không hợp lệ.
     */
    private static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return dateTime.format(formatter);
    }
}
