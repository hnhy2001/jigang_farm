package com.example.jingangfarmmanagement.model.response;

import com.example.jingangfarmmanagement.repository.entity.Cage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuantityPetRes {
    Cage cage;
    Long quantity = 0L;
}
