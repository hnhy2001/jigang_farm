package com.example.jingangfarmmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class News extends BaseEntity{
    @Column(name = "title")
    private String title;
    @Column(name = "description",columnDefinition = "LONGTEXT")
    private String description;
    @Column(name = "image",columnDefinition = "LONGTEXT")
    private String image;
    @Column(name = "content",columnDefinition = "LONGTEXT")
    private String content;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
