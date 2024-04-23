package com.example.jingangfarmmanagement.model.req;

import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import lombok.Data;

import java.util.List;

@Data
public class ChangeCageReq {
    List<Pet> petList;
    Cage cage;
}
