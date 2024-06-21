package com.example.jingangfarmmanagement.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.example.jingangfarmmanagement.exception.GlobalException;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.CageRepository;
import com.example.jingangfarmmanagement.repository.FarmRepository;
import com.example.jingangfarmmanagement.repository.MaterialsRepository;
import com.example.jingangfarmmanagement.repository.PetRepository;
import com.example.jingangfarmmanagement.repository.dto.PetFileImportDto;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.repository.entity.Materials;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.exception.GlobalException;
import com.example.jingangfarmmanagement.service.IOFileService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IOFileServiceImpl implements IOFileService {
    @Autowired
    CageRepository cageRepository;
    @Autowired
    FarmRepository farmRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    MaterialsRepository materialsRepository;


    @Override
    @Transactional
    public BaseResponse importPetsFromExcel(MultipartFile file) throws IOException {
        List<Pet> pets = new ArrayList<>();
        int totalPets = petRepository.findAll().size();
        int noPet = totalPets + 1;

        try {
            List<PetFileImportDto> petFileImportDtos = EasyExcel.read(file.getInputStream())
                    .head(PetFileImportDto.class)
                    .sheet()
                    .doReadSync();

            for (PetFileImportDto dto : petFileImportDtos) {
                if (dto.getFarmName() == null && dto.getCageName() != null && dto.getName() != null) {
                    break;
                }
                Farm farm = getFarm(dto.getFarmName());
                if (farm == null) {
                    farm = createNewFarm(dto.getFarmName());
                }

                Cage cage = getCage(dto.getCageName(), dto.getFarmName());
                if (cage == null) {
                    cage = createNewCage(dto.getCageName(), dto.getFarmName());
                }

                Pet pet = petRepository.findByCageAndFarmAndName(dto.getName(), dto.getCageName(), dto.getFarmName());
                if (pet != null) {
                    updatePet(pet, dto);
                } else {
                    pet = createNewPet(dto, cage, noPet);
                    noPet++;
                }
                pets.add(pet);
            }

            petRepository.saveAll(pets);
            return new BaseResponse(200, "OK", "Nhập dữ liệu thành công");

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
        return cageRepository.save(cage);
    }
    private Farm createNewFarm(String farmName) {
        Farm farm = new Farm();
        farm.setName(farmName);
        farm.setStatus(1);
        farm.setCreateDate(com.example.jingangfarmmanagement.uitl.DateUtil.getCurrenDateTime());
        return farmRepository.save(farm);
    }

    private void updatePet(Pet pet, PetFileImportDto dto) {
        pet.setType(dto.getType());
        pet.setAge(dto.getAge() != null ? Integer.parseInt(dto.getAge()) : 0);
        pet.setWeight(dto.getWeight() != null ? Double.parseDouble(dto.getWeight()) : 0);
        pet.setSex("Cái".equalsIgnoreCase(dto.getSex()) ? 0 : 1);
        pet.setUilness(dto.getUilness());
        pet.setParentDad(dto.getParentDad());
        pet.setParentMon(dto.getParentMom());
        pet.setStatus(1);
    }

    private Pet createNewPet(PetFileImportDto dto, Cage cage, int noPet) {
        Pet pet = new Pet();
        pet.setCode("VN_" + dto.getFarmName() + "_" + dto.getCageName() + "_" + noPet);
        pet.setName(dto.getName());
        pet.setType(dto.getType());
        pet.setAge(dto.getAge() != null ? Integer.parseInt(dto.getAge()) : 0);
        pet.setWeight(dto.getWeight() != null ? Double.parseDouble(dto.getWeight()) : 0);
        pet.setSex("Cái".equalsIgnoreCase(dto.getSex()) ? 0 : 1);
        pet.setCage(cage);
        pet.setUilness(dto.getUilness());
        pet.setParentDad(dto.getParentDad());
        pet.setParentMon(dto.getParentMom());
        pet.setStatus(1);
        return pet;
    }

    private Farm getFarm(String farmName){
        try{
            return farmRepository.findByName(farmName);
        }catch(Exception e){
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
