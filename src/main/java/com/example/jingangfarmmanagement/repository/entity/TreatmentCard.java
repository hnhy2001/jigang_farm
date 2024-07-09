package com.example.jingangfarmmanagement.repository.entity;

import com.example.jingangfarmmanagement.repository.entity.Enum.ETreatmentCardStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TreatmentCard extends BaseEntity{
    String code;
    String note;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "treatment_card_uiless",
            joinColumns = @JoinColumn(name = "treatment_card_id"),
            inverseJoinColumns = @JoinColumn(name = "uilness_id")
    )
    @JsonManagedReference
    private List<Uilness> uilnesses = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "treatment_card_pet",
            joinColumns = @JoinColumn(name = "treatment_card_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id")
    )
    @JsonManagedReference
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "treatmentCard", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ImageTreatmentCart> images;
}
