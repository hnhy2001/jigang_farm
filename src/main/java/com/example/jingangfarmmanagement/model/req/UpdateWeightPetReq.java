package com.example.jingangfarmmanagement.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWeightPetReq {
    List<Long> petIds;
    Double weight;
}
