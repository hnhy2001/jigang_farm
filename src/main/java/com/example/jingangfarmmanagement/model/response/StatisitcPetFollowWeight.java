package com.example.jingangfarmmanagement.model.response;

import com.example.jingangfarmmanagement.constants.Weight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisitcPetFollowWeight {
    Weight musty1 = new Weight(0,25,0);
    Weight musty2 = new Weight(25,50,0);
    Weight musty3 = new Weight(50,75,0);
    Weight musty4 = new Weight(75,100,0);
    Weight musty5 = new Weight(100,99999999,0);

}
