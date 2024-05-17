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

import javax.transaction.Transactional;
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
    @Transactional
    public void importPetsFromExcel(MultipartFile file,String farmCode) throws IOException {
        List<Pet> pets = new ArrayList<>();
        int totalPets=petRepository.findAll().size();
        int noPet=totalPets+1;
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0 ) {
                    continue;
                }
                Pet pet = new Pet();
                pet.setCode("VN_" + farmCode + "_" + getCellValue(row.getCell(5)) + "_" + noPet);
                pet.setName(getCellValueOrDefault(row.getCell(0)));
                pet.setType(getCellValueOrDefault(row.getCell(1)));
                pet.setAge(getCellNumericValueOrDefault(row.getCell(2)));
                pet.setWeight(getCellNumericValueOrDefault(row.getCell(3)));
                pet.setSex(getCellSexValueOrDefault(row.getCell(4)));
                pet.setCage(getCageFromCellOrDefault(row.getCell(5)));
                pet.setUilness(getCellValueOrDefault(row.getCell(6)));
                pets.add(pet);
                noPet++;
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
    private String getCellValueOrDefault(Cell cell) {
        return cell != null && cell.getCellType() != CellType.BLANK ? getCellValue(cell) : null;
    }

    private int getCellNumericValueOrDefault(Cell cell) {
        return cell != null && cell.getCellType() != CellType.BLANK ? (int) Double.parseDouble(getCellValue(cell)) : 0;
    }


    private int getCellSexValueOrDefault(Cell cell) {
        return cell != null && cell.getCellType() != CellType.BLANK && getCellValue(cell).equalsIgnoreCase("Cái") ? 0 : 1;
    }

    private Cage getCageFromCellOrDefault(Cell cell) throws BaseResponse {
        if (cell != null && cell.getCellType() != CellType.BLANK) {
            Cage cage = cageRepository.findByCode(getCellValue(cell));
            if (cage != null) {
                return cage;
            } else {
                throw GlobalException.notFoundException(getCellValue(cell));
            }
        } else {
            throw GlobalException.notFoundException("Cage code is null or blank");
        }
    }
}
