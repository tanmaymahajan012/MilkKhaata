package com.milkkhaata.service;

import com.milkkhaata.dto.request.MilkDeliveryRequest;
import com.milkkhaata.dto.response.MilkDeliveryResponse;
import com.milkkhaata.entities.Customer;
import com.milkkhaata.entities.MilkDelivery;
import com.milkkhaata.entities.User;
import com.milkkhaata.exception.ResourceNotFoundException;
import com.milkkhaata.repository.CustomerRepository;
import com.milkkhaata.repository.MilkDeliveryRepository;
import com.milkkhaata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MilkDeliveryService {

    private final MilkDeliveryRepository milkDeliveryRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public MilkDeliveryResponse createDelivery(
            MilkDeliveryRequest request,
            String email
    ) {

        User user = getUserByEmail(email);

        Customer customer = customerRepository
                .findByIdAndUser(request.getCustomerId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found")
                );

        if (!customer.getActive()) {
            throw new ResourceNotFoundException("Customer not found");
        }

        MilkDelivery delivery = MilkDelivery.builder()
                .deliveryDate(request.getDeliveryDate())
                .liters(request.getLiters())
                .ratePerLiter(request.getRatePerLiter())
                .shift(request.getShift())
                .customer(customer)
                .user(user)
                .build();

        MilkDelivery savedDelivery =
                milkDeliveryRepository.save(delivery);

        return mapToResponse(savedDelivery);
    }

    public List<MilkDeliveryResponse> getAllDeliveries(
            String email
    ) {

        User user = getUserByEmail(email);

        return milkDeliveryRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MilkDeliveryResponse getDeliveryById(
            Long deliveryId,
            String email
    ) {

        User user = getUserByEmail(email);

        MilkDelivery delivery = milkDeliveryRepository
                .findByIdAndUser(deliveryId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Milk delivery not found"
                        )
                );

        return mapToResponse(delivery);
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private MilkDeliveryResponse mapToResponse(
            MilkDelivery delivery
    ) {

        return new MilkDeliveryResponse(
                delivery.getId(),
                delivery.getCustomer().getId(),
                delivery.getCustomer().getName(),
                delivery.getDeliveryDate(),
                delivery.getLiters(),
                delivery.getRatePerLiter(),
                delivery.getShift(),
                delivery.getCreatedAt(),
                delivery.getUpdatedAt()
        );
    }

    public MilkDeliveryResponse updateDelivery(
            Long deliveryId,
            MilkDeliveryRequest request,
            String email
    ) {

        User user = getUserByEmail(email);

        MilkDelivery delivery = milkDeliveryRepository
                .findByIdAndUser(deliveryId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Milk delivery not found"
                        )
                );

        Customer customer = customerRepository
                .findByIdAndUser(request.getCustomerId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found")
                );

        if (!customer.getActive()) {
            throw new ResourceNotFoundException("Customer not found");
        }

        delivery.setCustomer(customer);
        delivery.setDeliveryDate(request.getDeliveryDate());
        delivery.setLiters(request.getLiters());
        delivery.setRatePerLiter(request.getRatePerLiter());
        delivery.setShift(request.getShift());

        MilkDelivery updatedDelivery =
                milkDeliveryRepository.save(delivery);

        return mapToResponse(updatedDelivery);
    }

    public void deleteDelivery(
            Long deliveryId,
            String email
    ) {

        User user = getUserByEmail(email);

        MilkDelivery delivery = milkDeliveryRepository
                .findByIdAndUser(deliveryId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Milk delivery not found"
                        )
                );

        milkDeliveryRepository.delete(delivery);
    }

    public List<MilkDeliveryResponse> getDeliveriesByCustomer(
            Long customerId,
            String email
    ) {

        User user = getUserByEmail(email);

        Customer customer = customerRepository
                .findByIdAndUser(customerId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found")
                );

        return milkDeliveryRepository
                .findByCustomerAndUser(customer, user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<MilkDeliveryResponse> getDeliveriesByCustomerAndMonth(
            Long customerId,
            Integer month,
            Integer year,
            String email
    ) {

        User user = getUserByEmail(email);

        Customer customer = customerRepository
                .findByIdAndUser(customerId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found")
                );

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(
                startDate.lengthOfMonth()
        );

        return milkDeliveryRepository
                .findByCustomerAndUserAndDeliveryDateBetween(
                        customer,
                        user,
                        startDate,
                        endDate
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}