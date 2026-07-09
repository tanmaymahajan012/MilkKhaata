package com.milkkhaata.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "milk_production_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilkProductionEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Entry date is required")
    @Column(nullable = false)
    private LocalDate entryDate;

    @NotNull(message = "Milk quantity is required")
    @DecimalMin(value = "0.1", message = "Milk quantity must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal liters;

    @NotNull(message = "Rate per liter is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Rate must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal ratePerLiter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Shift shift;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}