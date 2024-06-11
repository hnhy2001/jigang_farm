package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.exception.GlobalException;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.CageRepository;
import com.example.jingangfarmmanagement.repository.MaterialsRepository;
import com.example.jingangfarmmanagement.repository.PetRepository;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Materials;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.exception.GlobalException;
import com.example.jingangfarmmanagement.service.IOFileService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
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
    PetRepository petRepository;
    @Autowired
    MaterialsRepository materialsRepository;

    @Override
    @Transactional
    public BaseResponse importPetsFromExcel(MultipartFile file,String farmCode,String cageCode) throws IOException {
        List<Pet> pets = new ArrayList<>();
        int totalPets = petRepository.findAll().size();
        int noPet = totalPets + 1;
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                if(petRepository.findByName(getCellValueOrDefault(row.getCell(0)))!=null){
                    continue;
                }
                if(getCellValueOrDefault(row.getCell(0))==null){
                    break;
                }
                Pet pet = new Pet();
                pet.setCode("VN_" + farmCode + "_" + cageCode + "_" + noPet);
                pet.setName(getCellValueOrDefault(row.getCell(0)));
                pet.setType(getCellValueOrDefault(row.getCell(1)));
                pet.setAge(getCellNumericValueOrDefault(row.getCell(2)));
                pet.setWeight(getCellNumericValueOrDefault(row.getCell(3)));
                pet.setSex(getCellSexValueOrDefault(row.getCell(4)));
                pet.setCage(getCage(cageCode));
                pet.setUilness(getCellValueOrDefault(row.getCell(5)));
                pet.setParentDad(getCellValueOrDefault(row.getCell(6)));
                pet.setParentMon(getCellValueOrDefault(row.getCell(7)));
                pet.setStatus(1);
                pets.add(pet);
                noPet++;
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
        petRepository.saveAll(pets);
        return new BaseResponse(200, "OK", "Nhập dữ liệu thành công");
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

    private Cage getCage(String cageCode) {
        if (cageCode != null) {
           return cageRepository.findByCode(cageCode);
        }
        return null;
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
