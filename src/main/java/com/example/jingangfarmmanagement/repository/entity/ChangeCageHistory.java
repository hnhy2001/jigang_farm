package com.example.jingangfarmmanagement.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeCageHistory extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
    private String farmNameFrom;
    private String cageNameFrom;
    private String farmNameTo;
    private String cageNameTo;
}
