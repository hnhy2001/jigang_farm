package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image_cage_note")
public class ImageCageNote extends BaseEntity {
    @Column(name = "url", length = 9999999)
    private String url;

    @ManyToOne
    @JoinColumn(name = "cage_note")
    @JsonBackReference
    private CageNoteHistory cageNoteHistory;
}
