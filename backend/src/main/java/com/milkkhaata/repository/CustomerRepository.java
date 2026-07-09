package com.milkkhaata.repository;

import com.milkkhaata.entities.Customer;
import com.milkkhaata.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByUser(User user);

    Optional<Customer> findByIdAndUser(Long id, User user);

    List<Customer> findByUserAndActiveTrue(User user);

    long countByUserAndActiveTrue(User user);

}