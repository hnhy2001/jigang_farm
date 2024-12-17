package com.example.jingangfarmmanagement.model.req;

import com.example.jingangfarmmanagement.repository.entity.Cage;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeCageByNameReq {
    private String cageName;
    private String farmName;
    private String petName;
    private Cage cage;
}
