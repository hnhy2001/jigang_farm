package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.exception.GlobalException;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.CageRepository;
import com.example.jingangfarmmanagement.repository.PetRepository;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.exception.GlobalException;
import com.example.jingangfarmmanagement.service.IOFileService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IOFileServiceImpl implements IOFileService {
    @Autowired
    CageRepository cageRepository;
    @Autowired
    PetRepository petRepository;
    @Override
    public void importPetsFromExcel(MultipartFile file) throws IOException {
        List<Pet> pets = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0 || row.getRowNum()==1) {
                    continue;
                }
                Pet pet = new Pet();

                pet.setCode(getCellValue(row.getCell(0))!=null? getCellValue(row.getCell(0)):null);
                pet.setName(getCellValue(row.getCell(1))!=null? getCellValue(row.getCell(1)):null);
                pet.setType(getCellValue(row.getCell(2))!=null? getCellValue(row.getCell(2)):null);
                pet.setAge((int) Double.parseDouble(getCellValue(row.getCell(3))));
                pet.setWeight(Double.parseDouble(getCellValue(row.getCell(4))));
                pet.setSex(getCellValue(row.getCell(5)).equalsIgnoreCase("Cái") ?0 : 1);
                if(cageRepository.findByCode(getCellValue(row.getCell(6)))!=null){
                    Cage cage = cageRepository.findByCode(getCellValue(row.getCell(6)));
                    pet.setCage(cage);
                }
                else{
                    throw GlobalException.notFoundException(getCellValue(row.getCell(6)));
                }
                pet.setUilness(getCellValue(row.getCell(7)));
                pets.add(pet);
            }
        } catch (BaseResponse e) {
            throw new RuntimeException(e);
        }
        petRepository.saveAll(pets);
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
}
