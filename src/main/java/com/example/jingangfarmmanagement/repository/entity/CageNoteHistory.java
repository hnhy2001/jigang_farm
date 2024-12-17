package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CageNoteHistory extends BaseEntity{
    String note;
    int warning;
    int isReaction;
    @ManyToOne
    @JoinColumn(name = "cage", nullable = false)
    private Cage cage;

    @OneToMany(mappedBy = "cageNoteHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ImageCageNote> images;
}
