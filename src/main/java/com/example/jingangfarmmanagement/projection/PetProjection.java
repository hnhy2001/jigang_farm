package com.example.jingangfarmmanagement.projection;

import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.PetType;
import com.example.jingangfarmmanagement.repository.entity.Uilness;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public interface PetProjection {
    Long getId();
    String getCode();
    String getName();
    int getAge();
    double getWeight();
    String getWarning();
    int getSex();
    String getParentDad();
    String getParentMom();
    PetTypeWithId getType();
    UilnessWithId getUilness();
    CageWithId getCage();


    interface PetTypeWithId{
        Long getId();
    }

    interface UilnessWithId{
        Long getId();
        UilnessTypeWithId getUilnessType();

        interface UilnessTypeWithId{
            Long getId();
            String getCode();
            String getName();
            int getScore();
        }
    }

    interface CageWithId{
        Long getId();
        String getName();
    }
}
