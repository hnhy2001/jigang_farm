package com.example.jingangfarmmanagement.repository.dto;

import lombok.Getter;
import lombok.Setter;
import com.alibaba.excel.annotation.ExcelProperty;
@Getter
@Setter
public class PetFileImportDto {
    @ExcelProperty(value="Trại")
    private String farmName;
    @ExcelProperty(value="Chuồng")
    private String cageName;
    @ExcelProperty(value="Tên vật nuôi")
    private String name;
    @ExcelProperty(value="Loại")
    private String type;
    @ExcelProperty(value="Tháng tuổi")
    private String age;
    @ExcelProperty(value="Cân nặng")
    private String weight;
    @ExcelProperty(value="Giới tính")
    private String sex;
    @ExcelProperty(value="Tình trạng bệnh")
    private String uilness;
    @ExcelProperty(value="Tình trạng vật nuôi")
    private String petCondition;
    @ExcelProperty(value="Cha")
    private String parentDad;
    @ExcelProperty(value="Mẹ")
    private String parentMom;
}
