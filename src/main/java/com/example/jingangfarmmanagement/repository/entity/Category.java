package com.example.jingangfarmmanagement.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category extends BaseEntity{
    @Column(name = "title")
    private String title;
    @Column(name = "path")
    private String path;
    @Column(name = "parent_id")
    private String parentId;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy="category")
    private Set<News> news;
    @ManyToMany(mappedBy = "categories")
    private Set<MenuConfig> menuConfigs;
    @ManyToMany(mappedBy = "categories")
    private Set<Category> categories;
    @Transient
    private List<Category> children = new ArrayList<>();

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }
}
