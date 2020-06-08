package com.capstone.ecommerce.model;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 6)
    private String color;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String type;

    @Column(precision = 5, scale = 2, nullable = false)
    private double price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "tinyint(1) default 0", name = "on_special")
    private Boolean special;


    @Column(columnDefinition = "int UNSIGNED default 1", nullable = false)
    private long quantity;

    @Column
    private String image;


    @ManyToMany
            (fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.ALL
            })
    @JoinTable(name = "product_categories",
            joinColumns = { @JoinColumn(name = "product_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") })
    private List<Categories> categories;


    public Product() {
    }

    public Product(String name, String color, String size, String type, double price, String description, Boolean onSpecial, Long quantity) {
        this.name = name;
        this.color = color;
        this.size = size;
        this.type = type;
        this.price = price;
        this.description = description;
        this.special = onSpecial;
        this.quantity = quantity;
//        this.image = image;
    }

    public Product(long id, String image, List<Categories> categories, String name, String color, String size,
                   String type, double price, String description, Boolean onSpecial, Long quantity) {
        this.id = id;
        this.image = image;
        this.categories = categories;
        this.name = name;
        this.color = color;
        this.size = size;
        this.type = type;
        this.price = price;
        this.description = description;
        this.special = onSpecial;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSpecial() {
        return special;
    }

    public void setSpecial(Boolean special) {
        this.special = special;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //
//    public Transaction getTransaction() {
//        return transaction;
//    }
//
//    public void setTransaction(Transaction transaction) {
//        this.transaction = transaction;
//    }


    public List<Categories> getCategories() {
        return categories;
    }

    @Transactional
    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }


}

