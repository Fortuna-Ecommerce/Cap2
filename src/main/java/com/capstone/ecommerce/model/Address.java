package com.capstone.ecommerce.model;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(nullable = false, name = "address_type")
        private String addresstype;

        @Column(nullable = false, length = 75, name = "first_name")
        private String firstname;

        @Column(nullable = false, length = 75, name = "last_name")
        private String lastname;

        @Column(nullable = false, length = 75)
        private String street1;

        @Column(nullable = true, length = 75)
        private String street2;

        @Column(nullable = false, length = 40)
        private String city;

        @Column(nullable = false, length = 2)
        private String state;

        @Column(nullable = false, length = 5, name = "zip_code")
        private int zipcode;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        public Address() {
        }

        public Address(String address_type, String first_name, String last_name, String street1, String street2,
                       String city,
                       String state, int zipCode) {
                this.addresstype = address_type;
                this.firstname = first_name;
                this.lastname = last_name;
                this.street1 = street1;
                this.street2 = street2;
                this.city = city;
                this.state = state;
                this.zipcode = zipCode;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getFirstname() {
                return firstname;
        }

        public void setFirstname(String first_name) {
                this.firstname = first_name;
        }

        public String getLastname() {
                return lastname;
        }

        public void setLastname(String last_name) {
                this.lastname = last_name;
        }

        public String getAddresstype() {
                return addresstype;
        }

        public void setAddresstype(String address_type) {
                this.addresstype = address_type;
        }

        public String getStreet1() {
                return street1;
        }

        public void setStreet1(String street1) {
                this.street1 = street1;
        }

        public String getStreet2() {
                return street2;
        }

        public void setStreet2(String street2) {
                this.street2 = street2;
        }

        public String getCity() {
                return city;
        }

        public void setCity(String city) {
                this.city = city;
        }

        public String getState() {
                return state;
        }

        public void setState(String state) {
                this.state = state;
        }

        public int getZipcode() {
                return zipcode;
        }

        public void setZipcode(int zipCode) {
                this.zipcode = zipCode;
        }

        public User getUser() {
                return user;
        }

        public void setUser(User user) {
                this.user = user;
        }

}

