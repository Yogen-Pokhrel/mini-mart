package com.minimart.category.entity;


import com.minimart.product.entity.Product;
import com.minimart.productattribute.entity.ProductAttribute;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String slug;
    private String description;
    private String image;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ProductCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<ProductCategory> children = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_category_attributes",
            joinColumns = @JoinColumn(name = "product_category_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private List<ProductAttribute> attributes = new ArrayList<>();


}
