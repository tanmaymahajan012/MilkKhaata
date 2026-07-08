package com.milkkhaata.repository;

import com.milkkhaata.entities.MilkProductionEntry;
import com.milkkhaata.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MilkProductionEntryRepository extends JpaRepository<MilkProductionEntry, Long> {

    List<MilkProductionEntry> findByUser(User user);

    Optional<MilkProductionEntry> findByIdAndUser(Long id, User user);

    List<MilkProductionEntry> findByUserAndEntryDateBetween(
            User user,
            LocalDate startDate,
            LocalDate endDate
    );

}