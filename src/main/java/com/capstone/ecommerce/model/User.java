package com.capstone.ecommerce.model;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity //Interacts with databases
@Table(name="users")//Creates Table in database
public class User {
    // Generates column named Id in database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //Creates new column named username that is not null, unique, and a varchar(50)
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 75)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String stripeToken;

    //Makes a new column declares the type of Boolean, sets default value to false, which is named isAdmin
    @Column(columnDefinition = "tinyint(1) default 0", nullable = false)
    private Boolean isAdmin;

    //Makes a new Table of Transactions which is based on columns from other tables (user), has a one to many relationship between user and transaction.
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Transaction> transactions;

    //Declares when accessing addresses it distinguishes between billing and shipping and cascades each address respectively (if shipping address is changed, shipping address is updated)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Where(clause = "address_type = 'shipping'")
    private List<Address> ship_address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Where(clause = "address_type = 'billing'")
    private List<Address> bill_address;

    public User() {
    }

    //Creates the User object and specifies what the User object consists of
    public User(String username, String email, String password, Boolean isAdmin) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    //Creates a copy of the User object for user manipulation (update)
    public User(User copy) {
        id = copy.id;
        email = copy.email;
        username = copy.username;
        password = copy.password;
        isAdmin = copy.isAdmin;
        stripeToken = copy.stripeToken;
    }






    // Getters and Setters for User Model


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Address> getShip_address() {
        return ship_address;
    }

    public void setShip_address(List<Address> ship_address) {
        this.ship_address = ship_address;
    }

    public List<Address> getBill_address() {
        return bill_address;
    }

    public void setBill_address(List<Address> bill_address) {
        this.bill_address = bill_address;
    }
}
