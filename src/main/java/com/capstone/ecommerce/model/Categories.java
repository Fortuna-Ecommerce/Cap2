package com.capstone.ecommerce.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String category;

    @ManyToMany
            (fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.ALL
            },
            mappedBy = "categories")
    private List<Product> products;

    public Categories() {
    }

    public Categories(String category) {
        this.category = category;
    }

    public Categories(long id, String category, List<Product> products) {
        this.id = id;
        this.category = category;
        this.products = products;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String genre) {
        this.category = genre;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
