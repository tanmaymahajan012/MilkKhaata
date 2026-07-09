package com.milkkhaata.repository;

import com.milkkhaata.entities.Bill;
import com.milkkhaata.entities.BillStatus;
import com.milkkhaata.entities.Customer;
import com.milkkhaata.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByCustomer(Customer customer);

    Optional<Bill> findByCustomerAndMonthAndYearAndUser(
            Customer customer,
            Integer month,
            Integer year,
            User user
    );

    Optional<Bill> findByIdAndUser(
            Long id,
            User user
    );

    List<Bill> findByUser(User user);

    List<Bill> findByCustomerAndUser(
            Customer customer,
            User user
    );

    List<Bill> findByUserAndStatus(
            User user,
            BillStatus status
    );

    List<Bill> findByUserAndMonthAndYearAndStatus(
            User user,
            Integer month,
            Integer year,
            BillStatus status
    );

    List<Bill> findByUserAndYearAndStatus(
            User user,
            Integer year,
            BillStatus status
    );

}