package com.capstone.ecommerce.repositories;

import com.capstone.ecommerce.model.Address;
import com.capstone.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    void save(List<Address> addresses);
    List<Address> findByAddresstype(String string);
    Address findByFirstname(String string);
    Address findByUserAndAddresstype(User user, String string);
//    Object save(Address address);
//    List<Address> findByUserIdAndAddress_type(long id, String string);
//    Address findByAddress_typeAndUserIdtypeAndUserId();
//    Address findByFirst_nameAnAndLast_name();

}
