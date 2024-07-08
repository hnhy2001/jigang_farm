package com.example.jingangfarmmanagement.repository.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
}
