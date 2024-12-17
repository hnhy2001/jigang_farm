package com.example.jingangfarmmanagement.model.req;

import com.example.jingangfarmmanagement.repository.entity.Materials;
import com.example.jingangfarmmanagement.repository.entity.User;
import com.example.jingangfarmmanagement.repository.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBOLReq {
    private Long id;
    private int type;
    private Long debt;
    private User user;
    private String code;
    private List<MaterialsReq> materilasList;
}
