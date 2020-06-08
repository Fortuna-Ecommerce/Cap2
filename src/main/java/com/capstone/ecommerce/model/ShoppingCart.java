package com.capstone.ecommerce.model;

import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.*;
import java.io.*;

public class ShoppingCart extends ArrayList<Product> {

    private Product product;


    @ModelAttribute("products")
    public ShoppingCart products(){
        return new ShoppingCart();
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
//
//    // The shopping cart items are stored in a Vector.
//    protected Vector products;
//
//    public ShoppingCart() {
//        products = new Vector();
//    }
//
//    /**
//     * Returns a Vector containing the items in the cart. The Vector
//     * returned is a clone, so modifying the vector won't affect the
//     * contents of the cart.
//     */
//    public Vector getProducts() {
//        return (Vector) products.clone();
//    }
//
//    public void addProduct(Product newProduct) {
//        products.addElement(newProduct);
//    }
//
//    public void removeProduct(int productIndex) {
//        products.removeElementAt(productIndex);
//    }


}
