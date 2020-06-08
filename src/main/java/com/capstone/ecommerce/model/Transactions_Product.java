package com.capstone.ecommerce.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Transactions_Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = {
            CascadeType.MERGE
    })
    @JoinColumn(name = "transaction_id")
    Transaction transaction;

    @ManyToOne
            (cascade = {
            CascadeType.MERGE
    })
    @JoinColumn(name = "product_id")
    Product product;


    long quantity;


    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transactions_Product that = (Transactions_Product) o;
        return quantity == that.quantity &&
                Objects.equals(id, that.id) &&
                Objects.equals(transaction, that.transaction) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transaction, product, quantity);
    }
}
