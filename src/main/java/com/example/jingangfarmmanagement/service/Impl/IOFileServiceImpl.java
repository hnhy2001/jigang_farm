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
import com.example.jingangfarmmanagement.repository.entity.Materials;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.exception.GlobalException;
import com.example.jingangfarmmanagement.service.IOFileService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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




//    @Transactional
//    public BaseResponse importPetsFromExcel(MultipartFile file) throws IOException {
//        List<Pet> pets = new ArrayList<>();
//        int totalPets = petRepository.findAll().size();
//        int noPet = totalPets + 1;
//        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
//            Sheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                if (row.getRowNum() == 0) {
//                    continue;
//                }
////                if(petRepository.findByName(getCellValueOrDefault(row.getCell(2)))!=null){
////                    continue;
////                }
//                if(getCellValueOrDefault(row.getCell(0))==null && getCellValueOrDefault(row.getCell(1))!=null && getCellValueOrDefault(row.getCell(2))!=null ){
//                    break;
//                }
//                Pet pet = new Pet();
//                pet.setCode("VN_" + getCellValueOrDefault(row.getCell(0)) + "_" + getCellValueOrDefault(row.getCell(1)) + "_" + noPet);
//                pet.setName(getCellValueOrDefault(row.getCell(2)));
//                pet.setType(getCellValueOrDefault(row.getCell(3)));
//                pet.setAge(getCellNumericValueOrDefault(row.getCell(4)));
//                pet.setWeight(getCellDoubleValueOrDefault(row.getCell(5)));
//                pet.setSex(getCellSexValueOrDefault(row.getCell(6)));
//                if(getCage(getCellValueOrDefault(row.getCell(1)),getCellValueOrDefault(row.getCell(0)))!=null){
//                    pet.setCage(getCage(getCellValueOrDefault(row.getCell(1)),getCellValueOrDefault(row.getCell(0))));
//                }else{
//                    return new BaseResponse(500,"Tên chuồng trại không hợp lệ","chuồng "+ "-" +getCellValueOrDefault(row.getCell(1))+" trại "+"-"+ getCellValueOrDefault(row.getCell(0)) + "không hợp lệ");
//                }
//                pet.setUilness(getCellValueOrDefault(row.getCell(7)));
//                pet.setParentDad(getCellValueOrDefault(row.getCell(8)));
//                pet.setParentMon(getCellValueOrDefault(row.getCell(9)));
//                pet.setStatus(1);
//                pets.add(pet);
//                noPet++;
//            }
//        } catch (IOException e) {
//            throw new IOException(e);
//        }
//        petRepository.saveAll(pets);
//        return new BaseResponse(200, "OK", "Nhập dữ liệu thành công");
//    }

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
                // Check if the cage exists
                Cage cage = getCage(dto.getCageName(), dto.getFarmName());
                if (cage == null) {
                    // Create new cage if it doesn't exist
                    cage = new Cage();
                    cage.setName(dto.getCageName());
                    cage.setFarm(farmRepository.findByName(dto.getFarmName()));
                    // Save the new cage
                    cage = cageRepository.save(cage);
                }

                // Check if the pet already exists
                Pet existingPet = petRepository.findByCageAndFarmAndName(dto.getName(),dto.getCageName(), dto.getFarmName());

                if (existingPet != null) {
                    // Update existing pet details
                    existingPet.setType(dto.getType());
                    existingPet.setAge(dto.getAge()!=null ? Integer.parseInt(dto.getAge()): 0);
                    existingPet.setWeight(dto.getWeight()!=null ?Double.parseDouble(dto.getWeight()):0);
                    existingPet.setSex(dto.getSex() != null && dto.getSex().equalsIgnoreCase("Cái") ? 0 : 1);
                    existingPet.setUilness(dto.getUilness());
                    existingPet.setParentDad(dto.getParentDad());
                    existingPet.setParentMon(dto.getParentMom());
                    existingPet.setStatus(1);
                    pets.add(existingPet);
                } else {
                    // Create a new pet
                    Pet pet = new Pet();
                    pet.setCode("VN_" + dto.getFarmName() + "_" + dto.getCageName() + "_" + noPet);
                    pet.setName(dto.getName());
                    pet.setType(dto.getType());
                    pet.setAge(dto.getAge()!=null ? Integer.parseInt(dto.getAge()): 0);
                    pet.setWeight(dto.getWeight()!=null ? Double.parseDouble(dto.getWeight()):0);
                    pet.setSex(dto.getSex() != null && dto.getSex().equalsIgnoreCase("Cái") ? 0 : 1);
                    if (getCage(dto.getCageName(), dto.getFarmName()) != null) {
                        pet.setCage(getCage(dto.getCageName(), dto.getFarmName()));
                    } else {
                        return new BaseResponse(500, "Tên chuồng trại không hợp lệ", "chuồng " + "-" + dto.getCageName() + " trại " + "-" + dto.getFarmName() + " không hợp lệ");
                    }
                    pet.setUilness(dto.getUilness());
                    pet.setParentDad(dto.getParentDad());
                    pet.setParentMon(dto.getParentMom());
                    pet.setStatus(1);
                    pets.add(pet);
                    noPet++;
                }
            }
            petRepository.saveAll(pets);
            return new BaseResponse(200, "OK", "Nhập dữ liệu thành công");

        } catch (IOException e) {
            throw new IOException(e);
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

    private Long getDateCellValueOrDefault(Cell cell) {
        if (cell != null && cell.getCellType() != CellType.BLANK && cell.getCellType() == CellType.NUMERIC) {
            Date date = cell.getDateCellValue();
            if (date != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                return Long.parseLong(dateFormat.format(date));
            }
        }
        return null;
    }

    private int getCellNumericValueOrDefault(Cell cell) {
        return cell != null && cell.getCellType() != CellType.BLANK ? (int) Double.parseDouble(getCellValue(cell)) : 0;
    }
    private double getCellDoubleValueOrDefault(Cell cell) {
        return cell != null && cell.getCellType() != CellType.BLANK ?  Double.parseDouble(getCellValue(cell)) : 0;
    }


    private Long getNumericCellValueOrDefault(Cell cell) {
        if (cell != null && cell.getCellType() != CellType.BLANK && cell.getCellType() == CellType.NUMERIC) {
            // Chuyển đổi giá trị số thực thành số long
            return Double.valueOf(cell.getNumericCellValue()).longValue();
        }
        return null;
    }

    private int getCellSexValueOrDefault(Cell cell) {
        return cell != null && cell.getCellType() != CellType.BLANK && getCellValue(cell).equalsIgnoreCase("Cái") ? 0
                : 1;
    }

//    private Cage getCage(String cageName, String farmName) {
//        if (cageName == null || farmName == null) {
//            return null;
//        }
//        try {
//            int cage = (int) Double.parseDouble(cageName);
//            int farm = (int) Double.parseDouble(farmName);
//
//            return cageRepository.findByNameAndFarmName(String.valueOf(cage), String.valueOf(farm));
//        } catch (NumberFormatException e) {
//            return null;
//        }
//    }

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
