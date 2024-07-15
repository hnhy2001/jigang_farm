package com.example.jingangfarmmanagement.model.req;

import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import lombok.Data;

import java.util.List;

@Data
public class ChangeCageReq {
    List<Long> petList;
    Cage cage;
    String note;
}
