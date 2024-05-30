package com.example.jingangfarmmanagement.model.response;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.SecondaryTable;
import javax.persistence.Transient;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class MaterialRes {
    private String name;
    private String code;
    private String cargo;
    private String unit;
    private String price;
    private String note;
    private Long quantity;
    private Long id;
}
