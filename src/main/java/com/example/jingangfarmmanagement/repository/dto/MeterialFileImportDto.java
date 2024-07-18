package com.example.jingangfarmmanagement.repository.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeterialFileImportDto {
    @ExcelProperty(value="Nhóm hàng")
    private String productType;
    @ExcelProperty(value="Tên vật tư")
    private String name;
    @ExcelProperty(value="Liều lượng khuyến nghị")
    private String note;
    @ExcelProperty(value="Đơn vị")
    private String unit;
    @ExcelProperty(value="Chỉ định")
    private String indications;
    @ExcelProperty(value="Điều trị")
    private String treatment;
    @ExcelProperty(value = "Hạn sử dụng")
    private String expirationDate;


}
