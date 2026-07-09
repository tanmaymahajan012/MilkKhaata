package com.milkkhaata.service;

import com.milkkhaata.dto.request.CustomerRequest;
import com.milkkhaata.dto.response.CustomerResponse;
import com.milkkhaata.entities.Customer;
import com.milkkhaata.entities.User;
import com.milkkhaata.exception.ResourceNotFoundException;
import com.milkkhaata.exception.AccessDeniedException;
import com.milkkhaata.repository.CustomerRepository;
import com.milkkhaata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.milkkhaata.entities.Subscription;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    public CustomerResponse createCustomer(
            CustomerRequest request,
            String email
    ) {

        User user = getUserByEmail(email);

        Subscription subscription =
                subscriptionService.getActiveSubscription(user);

        long activeCustomerCount =
                customerRepository.countByUserAndActiveTrue(user);

        Integer customerLimit =
                subscription.getPlan().getCustomerLimit();

        if (activeCustomerCount >= customerLimit) {
            throw new AccessDeniedException(
                    "Customer limit reached for your current plan"
            );
        }

        Customer customer = Customer.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .active(true)
                .user(user)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return mapToResponse(savedCustomer);
    }

    public List<CustomerResponse> getAllCustomers(String email) {

        User user = getUserByEmail(email);

        return customerRepository.findByUserAndActiveTrue(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CustomerResponse getCustomerById(
            Long customerId,
            String email
    ) {

        User user = getUserByEmail(email);

        Customer customer = customerRepository
                .findByIdAndUser(customerId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found")
                );

        return mapToResponse(customer);
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private CustomerResponse mapToResponse(Customer customer) {

        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getActive(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

    public CustomerResponse updateCustomer(
            Long customerId,
            CustomerRequest request,
            String email
    ) {

        User user = getUserByEmail(email);

        Customer customer = customerRepository
                .findByIdAndUser(customerId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found")
                );

        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        Customer updatedCustomer = customerRepository.save(customer);

        return mapToResponse(updatedCustomer);
    }

    public void deleteCustomer(
            Long customerId,
            String email
    ) {

        User user = getUserByEmail(email);

        Customer customer = customerRepository
                .findByIdAndUser(customerId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found")
                );

        customer.setActive(false);

        customerRepository.save(customer);
    }
}