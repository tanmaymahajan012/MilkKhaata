package com.milkkhaata.repository;

import com.milkkhaata.entities.Customer;
import com.milkkhaata.entities.MilkDelivery;
import com.milkkhaata.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MilkDeliveryRepository
        extends JpaRepository<MilkDelivery, Long> {

    List<MilkDelivery> findByUser(User user);

    List<MilkDelivery> findByCustomerAndUser(
            Customer customer,
            User user
    );

    Optional<MilkDelivery> findByIdAndUser(
            Long id,
            User user
    );

    List<MilkDelivery> findByCustomerAndUserAndDeliveryDateBetween(
            Customer customer,
            User user,
            LocalDate startDate,
            LocalDate endDate
    );

    List<MilkDelivery> findByUserAndDeliveryDateBetween(
            User user,
            LocalDate startDate,
            LocalDate endDate
    );
}
