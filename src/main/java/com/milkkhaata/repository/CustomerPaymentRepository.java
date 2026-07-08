package com.milkkhaata.repository;

import com.milkkhaata.entities.Customer;
import com.milkkhaata.entities.CustomerPayment;
import com.milkkhaata.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerPaymentRepository
        extends JpaRepository<CustomerPayment, Long> {

    List<CustomerPayment> findByUser(User user);

    Optional<CustomerPayment> findByIdAndUser(
            Long id,
            User user
    );

    List<CustomerPayment> findByCustomerAndUser(
            Customer customer,
            User user
    );

    boolean existsByBillId(Long billId);

    List<CustomerPayment> findByUserAndPaymentDateBetween(
            User user,
            LocalDate startDate,
            LocalDate endDate
    );
}