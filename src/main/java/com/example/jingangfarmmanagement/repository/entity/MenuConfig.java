package com.example.jingangfarmmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuConfig extends BaseEntity{
    @Column(name = "news_id",columnDefinition = "TEXT")
    private String newsId;
    @Column(name = "quantity")
    private Long quantity;
    @ManyToMany
    @JoinTable(
            name = "menu_catygory",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

}
