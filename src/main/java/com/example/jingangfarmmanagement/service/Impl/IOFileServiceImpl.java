package com.example.jingangfarmmanagement.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.example.jingangfarmmanagement.exception.GlobalException;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.repository.dto.MeterialFileImportDto;
import com.example.jingangfarmmanagement.repository.dto.PetFileImportDto;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.exception.GlobalException;
import com.example.jingangfarmmanagement.service.IOFileService;
import com.example.jingangfarmmanagement.service.PetService;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IOFileServiceImpl implements IOFileService {
    @Autowired
    CageRepository cageRepository;
    @Autowired
    UilnessRepository uilnessRepository;
    @Autowired
    FarmRepository farmRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    MaterialsRepository materialsRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PetService petService;
    private int menNumber = 1;
    private int womenNumber = 2;

    @Override
    @Transactional
    public BaseResponse importPetsFromExcel(MultipartFile file) throws IOException {
        Pet menPet = petService.getMenPetByNumbersOfMonth(DateUtil.getYearNow() + DateUtil.getMonthNow());
        Pet womenPet = petService.getWomenPetByNumbersOfMonth(DateUtil.getYearNow() + DateUtil.getMonthNow());
        if (menPet != null){
            menNumber = Integer.parseInt(menPet.getName().substring(5,7)) + 2;
        }

        if (womenPet != null){
            womenNumber = Integer.parseInt(womenPet.getName().substring(5,7)) + 2;
        }
        List<Pet> pets = new ArrayList<>();
        List<Farm> farms = new ArrayList<>();
        List<Cage> cages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        int totalPets = petRepository.findAll().size();
        int noPet = totalPets + 1;

        // Batch size configuration
        int batchSize = 50;  // You can adjust this value based on your requirements

        try {
            List<PetFileImportDto> petFileImportDtos = EasyExcel.read(file.getInputStream())
                    .head(PetFileImportDto.class)
                    .sheet()
                    .doReadSync();

            boolean errorOccurred = false; // Flag to track if error occurred

            for (int i = 0; i < petFileImportDtos.size(); i++) {
                PetFileImportDto dto = petFileImportDtos.get(i);
                try {
                    // Validate fields
                    if (dto.getFarmName() == null || dto.getCageName() == null || dto.getName() == null) {
                        throw new IllegalArgumentException("Thiếu một trong các trường bắt buộc: tên trại, tên chuồng, hoặc tên vật nuôi");
                    }

                    // Clear uilnesses list for each pet import
                    List<Uilness> uilnesses = new ArrayList<>();

                    // Process uilnesses from DTO
                    String uilnessData = dto.getUilness();
                    if (uilnessData != null && !uilnessData.isEmpty()) {
                        String[] uilnessNames = uilnessData.split(",");
                        for (String uilnessName : uilnessNames) {
                            Uilness expertUilness = getUilness(uilnessName.trim());
                            if (expertUilness == null) {
                                Uilness newUilness = createNewUilness(uilnessName.trim());
                                uilnessRepository.save(newUilness);
                                uilnesses.add(newUilness);
                            } else {
                                uilnesses.add(expertUilness);
                            }
                        }
                    }

                    Farm farm = getFarm(dto.getFarmName());
                    if (farm == null) {
                        farm = createNewFarm(dto.getFarmName());
                        farms.add(farm);
                        farm = farmRepository.save(farm);
                    } else if (farm.getStatus() != 1) {
                        throw new IllegalArgumentException("Trại " + farm.getName() + " không hoạt động");
                    }

                    Cage cage = getCage(dto.getCageName(), dto.getFarmName());
                    if (cage == null) {
                        cage = createNewCage(dto.getCageName(), dto.getFarmName());
                        cages.add(cage);
                        cage = cageRepository.save(cage);
                    } else if (cage.getStatus() != 1) {
                        throw new IllegalArgumentException("Chuồng " + cage.getName() + " không hoạt động");
                    }

                    // Check if the pet already exists in the pets list
                    boolean petFound = false;
                    for (Pet existingPet : pets) {
                        if (existingPet.getName().equals(dto.getName()) &&
                                existingPet.getCage().getName().equals(dto.getCageName()) &&
                                existingPet.getCage().getFarm().getName().equals(dto.getFarmName())) {
                            throw new IllegalArgumentException("Trùng vật nuôi cùng trại cùng chuồng");
                        }
                    }

                    // If not found in the current batch, check database for existing pet
                    if (!petFound) {
                        Pet pet = petRepository.findByCageAndFarmAndName(dto.getName(), dto.getCageName(), dto.getFarmName());
                        if (pet != null) {
                            updatePet(pet, dto, uilnesses);
                        } else {
                            pet = createNewPet(dto, cage, noPet, uilnesses);
                            noPet++;
                        }
                        pets.add(pet);
                    }

                    // Batch processing: save every batchSize pets
                    if ((i + 1) % batchSize == 0 || i == petFileImportDtos.size() - 1) {
                        petRepository.saveAll(pets);
                        farms.clear();
                        cages.clear();
                        pets.clear();

                        // Flush and clear to manage memory
                        entityManager.flush();
                        entityManager.clear();
                    }
                } catch (IllegalArgumentException e) {
                    // Return BaseResponse with the error message immediately
                    String errorMessage = "File nhập liệu lỗi ở dòng " + (i + 1) + ": " + e.getMessage();
                    System.err.println(errorMessage);
                    errorMessages.add(errorMessage);
                    errorOccurred = true; // Set error flag
                    break; // Stop processing further rows
                } catch (Exception e) {
                    // Log the error with the row information
                    String errorMessage = "File nhập liệu lỗi ở dòng" + (i + 1) + ": " + e.getMessage();
                    System.err.println(errorMessage);
                    errorMessages.add(errorMessage);
                }
            }

            // Save any remaining entities if no error occurred
            if (!errorOccurred) {
                if (!farms.isEmpty()) {
                    farmRepository.saveAll(farms);
                }
                if (!cages.isEmpty()) {
                    cageRepository.saveAll(cages);
                }
                if (!pets.isEmpty()) {
                    petRepository.saveAll(pets);
                }
            }

            // Check if there were any errors during processing
            if (errorOccurred) {
                return new BaseResponse(500, "Error", String.join("; ", errorMessages));
            } else {
                return new BaseResponse(200, "OK", "Nhập dữ liệu thành công");
            }
        } catch (IOException e) {
            throw new IOException("Đã có lỗi xảy ra ", e);
        }
    }



    private Cage createNewCage(String cageName, String farmName) {
        Cage cage = new Cage();
        cage.setName(cageName);
        cage.setFarm(farmRepository.findByName(farmName));
        cage.setStatus(1);
        cage.setCreateDate(com.example.jingangfarmmanagement.uitl.DateUtil.getCurrenDateTime());
        return cage;
    }

    private Farm createNewFarm(String farmName) {
        Farm farm = new Farm();
        farm.setName(farmName);
        farm.setStatus(1);
        farm.setCreateDate(com.example.jingangfarmmanagement.uitl.DateUtil.getCurrenDateTime());
        return farm;
    }
    private Uilness createNewUilness(String uilnessName) {
            Uilness uilness = new Uilness();
            uilness.setName(uilnessName);
            uilness.setScore(6);
            uilness.setStatus(1);
            uilness.setCreateDate(com.example.jingangfarmmanagement.uitl.DateUtil.getCurrenDateTime());
        return uilness;
    }

    private void updatePet(Pet pet, PetFileImportDto dto,List<Uilness> uilnesses) {
        pet.setType(dto.getType());
        pet.setAge(dto.getAge() != null ? dto.getAge() : "0");
        pet.setWeight(dto.getWeight() != null ? Double.parseDouble(dto.getWeight()) : 0);
        pet.setSex("Cái".equalsIgnoreCase(dto.getSex()) ? 0 : 1);
        List<String> uilnessNames= uilnesses.stream().map(Uilness::getName).collect(Collectors.toList());
        String result = uilnessNames.stream()
                .map(name -> name)
                .collect(Collectors.joining(","));
        pet.setUilness(result);
        pet.setParentDad(dto.getParentDad());
        pet.setParentMon(dto.getParentMom());
        pet.setUpdateHeathDate(DateUtil.getCurrenDateTime());
        pet.setPetCondition(dto.getUilness() != null ? 2 : 1);
        if(dto.getStatus()==null){
            dto.setStatus("");
        }
        switch (dto.getStatus().toLowerCase()) {
            case "":
                pet.setStatus(1);
                break;
            case "chửa":
                pet.setStatus(2);
                break;
            case "ôm con":
                pet.setStatus(3);
                break;
            case "thương tật vĩnh viễn":
                pet.setStatus(4);
                break;
        }
        pet.setUpdateHeathDate(DateUtil.getCurrenDateTime());
        pet.setLastDateUpdate(DateUtil.getCurrenDateTime());
        pet.setPregnantDateUpdate(DateUtil.getCurrenDateTime());
    }

    private Pet createNewPet(PetFileImportDto dto, Cage cage, int noPet,List<Uilness> uilnesses) {
        Pet pet = new Pet();
        pet.setCode("VN_" + dto.getFarmName() + "_" + dto.getCageName() + "_" + noPet);
        pet.setType(dto.getType());
        pet.setAge(dto.getAge() != null ? dto.getAge() : "0");
        pet.setWeight(dto.getWeight() != null ? Double.parseDouble(dto.getWeight()) : 0);
        pet.setSex("Cái".equalsIgnoreCase(dto.getSex()) ? 0 : 1);
        pet.setName(generateName(pet.getSex()));
        pet.setCage(cage);
        List<String> uilnessNames= uilnesses.stream().map(Uilness::getName).collect(Collectors.toList());
        String result = uilnessNames.stream()
                .map(name -> name)
                .collect(Collectors.joining(","));
        pet.setUilness(result);
        pet.setParentDad(dto.getParentDad());
        pet.setParentMon(dto.getParentMom());
        pet.setCreateDate(com.example.jingangfarmmanagement.uitl.DateUtil.getCurrenDateTime());
        pet.setPetCondition(dto.getUilness() != null ? 2 : 1);
        if(dto.getStatus()==null){
            dto.setStatus("");
        }
        switch (dto.getStatus().toLowerCase()) {
            case "":
                pet.setStatus(1);
                break;
            case "chửa":
                pet.setStatus(2);
                break;
            case "ôm con":
                pet.setStatus(3);
                break;
            case "thương tật vĩnh viễn":
                pet.setStatus(4);
                break;
        }

        pet.setUpdateHeathDate(DateUtil.getCurrenDateTime());
        pet.setLastDateUpdate(DateUtil.getCurrenDateTime());
        pet.setPregnantDateUpdate(DateUtil.getCurrenDateTime());
        return pet;
    }

    private Farm getFarm(String farmName) {
        try {
            return farmRepository.findByName(farmName);
        } catch (Exception e) {
            return null;
        }
    }

    private Cage getCage(String cageName, String farmName) {
        try {
            return cageRepository.findByNameAndFarmName(cageName, farmName);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    private Uilness getUilness(String uilness) {
        try {
            return uilnessRepository.findByName(uilness);
        } catch (NumberFormatException e) {
            return null;
        }
    }



    @Transactional
    public BaseResponse importMaterialsFromExcel(MultipartFile file) throws IOException {
        List<Materials> materials = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        int totalMaterials = materialsRepository.findAll().size();
        int noMaterial = totalMaterials + 1;

        // Batch size configuration
        int batchSize = 50;  // You can adjust this value based on your requirements

        try {
            List<MeterialFileImportDto> meterialFileImportDtos = EasyExcel.read(file.getInputStream())
                    .head(MeterialFileImportDto.class)
                    .sheet()
                    .doReadSync();

            for (int i = 0; i < meterialFileImportDtos.size(); i++) {
                MeterialFileImportDto dto = meterialFileImportDtos.get(i);
                try {
                    // Validate fields
                    if (dto.getName() == null ) {
                        throw new IllegalArgumentException("Thiếu một trong các trường bắt buộc: tên vật tư");
                    }
                    // Check if the pet already exists in the pets list
                    boolean materialFound = false;
                    for (Materials existingMaterials : materials) {
                        if (existingMaterials.getName().equals(dto.getName())) {
                            throw new IllegalArgumentException("Trùng vật tư");
                        }

                    }

                    // If not found in the current batch, check database for existing pet
                    if (!materialFound) {
                        Materials material = materialsRepository.findByName(dto.getName());
                        if (material != null) {
                            updateMaterial(material, dto);
                        } else {
                            material = createNewMaterial(dto);
                            noMaterial++;
                        }
                        materials.add(material);
                    }
                    // Batch processing: save every batchSize pets
                    if ((i + 1) % batchSize == 0 || i == materials.size() - 1) {
                        materialsRepository.saveAll(materials);
                        materials.clear();

                        // Flush and clear to manage memory
                        entityManager.flush();
                        entityManager.clear();
                    }
                } catch (IllegalArgumentException e) {
                    // Return BaseResponse with the error message immediately
                    String errorMessage = "File nhập liệu lỗi ở dòng " + (i + 1) + ": " + e.getMessage();
                    System.err.println(errorMessage);
                    errorMessages.add(errorMessage);
                } catch (Exception e) {
                    // Log the error with the row information
                    String errorMessage = "File nhập liệu lỗi ở dòng" + (i + 1) + ": " + e.getMessage();
                    System.err.println(errorMessage);
                    errorMessages.add(errorMessage);
                }
            }

            // Save any remaining entities
            if (!materials.isEmpty()) {
                materialsRepository.saveAll(materials);
            }

            // Check if there were any errors during processing
            if (errorMessages.isEmpty()) {
                return new BaseResponse(200, "OK", "Nhập dữ liệu thành công");
            } else {
                return new BaseResponse(500, "Error", String.join("; ", errorMessages));
            }
        } catch (IOException e) {
            throw new IOException("Đã có lỗi xảy ra ", e);
        }
    }
    private void updateMaterial(Materials materials, MeterialFileImportDto dto) {
        materials.setProductType(dto.getProductType());
        materials.setName(dto.getName());
        materials.setNote(dto.getNote());
        materials.setUnit(dto.getUnit());
        materials.setCargo(dto.getCargo());
        materials.setFirstInventory(dto.getFirstInventory());
        materials.setIndications(dto.getIndications());
        materials.setTreatment(dto.getTreatment());
        materials.setExpirationDate(!dto.getExpirationDate().isBlank() ? convertDateStringToLong(dto.getExpirationDate()):0L);
        materials.setStatus(1);
    }

    private Materials createNewMaterial( MeterialFileImportDto dto) {
        Materials materials = new Materials();
        materials.setProductType(dto.getProductType());
        materials.setName(dto.getName());
        materials.setNote(dto.getNote());
        materials.setUnit(dto.getUnit());
        materials.setIndications(dto.getIndications());
        materials.setCargo(dto.getCargo());
        materials.setFirstInventory(dto.getFirstInventory());
        materials.setTreatment(dto.getTreatment());
        materials.setExpirationDate(!dto.getExpirationDate().isBlank() ? convertDateStringToLong(dto.getExpirationDate()):0L);
        materials.setStatus(1);
        materials.setCreateDate(com.example.jingangfarmmanagement.uitl.DateUtil.getCurrenDateTime());
        return materials;
    }
    public static long convertDateStringToLong(String dateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDate date = LocalDate.parse(dateString, inputFormatter);
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIDNIGHT);
        String outputString = dateTime.format(outputFormatter);
        return Long.parseLong(outputString);
    }

    public String generateName(int sex) {
        String year = DateUtil.getYearNow();
        String month = DateUtil.getMonthNow();
        if (sex == 1) {
            String result = year + month + getNumber(menNumber);
            menNumber = menNumber + 2;
            return result;
        } else {
            String result = year + month + getNumber(womenNumber);
            womenNumber = womenNumber + 2;
            return result;
        }
    }

    public String getNumber(int number) {
        if (number < 10) {
            return "00" + number;
        }

        if (number < 100) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

}
