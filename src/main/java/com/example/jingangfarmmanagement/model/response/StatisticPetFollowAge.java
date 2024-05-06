package com.example.jingangfarmmanagement.model.response;

import com.example.jingangfarmmanagement.constants.Age;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticPetFollowAge {
    Age musty1 = new Age(0,1,0);
    Age musty2 = new Age(1,2,0);
    Age musty3 = new Age(2,5,0);
    Age musty4 = new Age(5,7,0);
    Age musty5 = new Age(7,10,0);
    Age musty6 = new Age(10,25,0);
    Age musty7 = new Age(25,50, 0);
    Age musty8 = new Age(50,100, 0);
    Age musty9 = new Age(100,150, 0);
    Age musty10 = new Age(150,9999999, 0);
}
