package com.example.jingangfarmmanagement.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.example.jingangfarmmanagement.exception.GlobalException;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.repository.dto.PetFileImportDto;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.exception.GlobalException;
import com.example.jingangfarmmanagement.service.IOFileService;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
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
import java.util.*;

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
    @Override
    @Transactional
    public BaseResponse importPetsFromExcel(MultipartFile file) throws IOException {
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

            for (int i = 0; i < petFileImportDtos.size(); i++) {
                PetFileImportDto dto = petFileImportDtos.get(i);
                try {
                    // Validate fields
                    if (dto.getFarmName() == null || dto.getCageName() == null || dto.getName() == null) {
                        throw new IllegalArgumentException("Thiếu một trong các trường bắt buộc: tên trại, tên chuồng, or tên vật nuôi");
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
                    }

                    Cage cage = getCage(dto.getCageName(), dto.getFarmName());
                    if (cage == null) {
                        cage = createNewCage(dto.getCageName(), dto.getFarmName());
                        cages.add(cage);
                        cage = cageRepository.save(cage);
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
                    String errorMessage = "Error processing row " + (i + 1) + ": " + e.getMessage();
                    System.err.println(errorMessage);
                    errorMessages.add(errorMessage);
                } catch (Exception e) {
                    // Log the error with the row information
                    String errorMessage = "Error processing row " + (i + 1) + ": " + e.getMessage();
                    System.err.println(errorMessage);
                    errorMessages.add(errorMessage);
                }
            }

            // Save any remaining entities
            if (!farms.isEmpty()) {
                farmRepository.saveAll(farms);
            }
            if (!cages.isEmpty()) {
                cageRepository.saveAll(cages);
            }
            if (!pets.isEmpty()) {
                petRepository.saveAll(pets);
            }

            // Check if there were any errors during processing
            if (errorMessages.isEmpty()) {
                return new BaseResponse(200, "OK", "Nhập dữ liệu thành công");
            } else {
                return new BaseResponse(500, "Error", String.join("; ", errorMessages));
            }
        } catch (IOException e) {
            throw new IOException("Error processing file", e);
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
            uilness.setScore(1);
            uilness.setStatus(1);
            uilness.setCreateDate(com.example.jingangfarmmanagement.uitl.DateUtil.getCurrenDateTime());
        return uilness;
    }

    private void updatePet(Pet pet, PetFileImportDto dto,List<Uilness> uilnesses) {
        pet.setType(dto.getType());
        pet.setAge(dto.getAge() != null ? Integer.parseInt(dto.getAge()) : 0);
        pet.setWeight(dto.getWeight() != null ? Double.parseDouble(dto.getWeight()) : 0);
        pet.setSex("Cái".equalsIgnoreCase(dto.getSex()) ? 0 : 1);
        pet.setUilness(uilnesses.toString());
        pet.setParentDad(dto.getParentDad());
        pet.setParentMon(dto.getParentMom());
        pet.setStatus(1);
    }

    private Pet createNewPet(PetFileImportDto dto, Cage cage, int noPet,List<Uilness> uilnesses) {
        Pet pet = new Pet();
        pet.setCode("VN_" + dto.getFarmName() + "_" + dto.getCageName() + "_" + noPet);
        pet.setName(dto.getName());
        pet.setType(dto.getType());
        pet.setAge(dto.getAge() != null ? Integer.parseInt(dto.getAge()) : 0);
        pet.setWeight(dto.getWeight() != null ? Double.parseDouble(dto.getWeight()) : 0);
        pet.setSex("Cái".equalsIgnoreCase(dto.getSex()) ? 0 : 1);
        pet.setCage(cage);
        pet.setUilness(uilnesses.toString());
        pet.setParentDad(dto.getParentDad());
        pet.setParentMon(dto.getParentMom());
        pet.setStatus(1);
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


    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }


    private String getCellValueOrDefault(Cell cell) {
        return cell != null && cell.getCellType() != CellType.BLANK ? getCellValue(cell) : null;
    }


    @Override
    @Transactional
    public BaseResponse importMaterialsFromExcel(MultipartFile file) throws IOException {
        List<Materials> materials = new ArrayList<>();
        int totalMaterials = materialsRepository.findAll().size();
        int noMaterials = totalMaterials + 1;
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                if(materialsRepository.findByName(getCellValueOrDefault(row.getCell(0)))!=null){
                    continue;
                }
                if(getCellValueOrDefault(row.getCell(0))==null){
                    break;
                }
                Materials material = new Materials();
                material.setCode("VN_" + noMaterials);
                material.setName(getCellValueOrDefault(row.getCell(0)));
                material.setNote(getCellValueOrDefault(row.getCell(1)));
                material.setUnit(getCellValueOrDefault(row.getCell(2)));
//                material.setExpirationDate(getDateCellValueOrDefault(row.getCell(3)));
                material.setStatus(1);
                materials.add(material);
                noMaterials++;
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
        materialsRepository.saveAllAndFlush(materials);
        return new BaseResponse(200, "OK", "Nhập dữ liệu thành công");
    }
}
