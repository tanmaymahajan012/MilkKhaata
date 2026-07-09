package com.milkkhaata.service;

import com.milkkhaata.dto.request.SubscriptionRequest;
import com.milkkhaata.dto.response.SubscriptionResponse;
import com.milkkhaata.entities.Plan;
import com.milkkhaata.entities.Subscription;
import com.milkkhaata.entities.User;
import com.milkkhaata.exception.AccessDeniedException;
import com.milkkhaata.exception.ResourceNotFoundException;
import com.milkkhaata.repository.PlanRepository;
import com.milkkhaata.repository.SubscriptionRepository;
import com.milkkhaata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    @Value("${app.dev-subscription-enabled:false}")
    private boolean devSubscriptionEnabled;

    @Transactional
    public SubscriptionResponse subscribe(
            SubscriptionRequest request,
            String email
    ) {

        if (!devSubscriptionEnabled) {
            throw new AccessDeniedException(
                    "Direct subscription activation is disabled"
            );
        }

        User user = getUserByEmail(email);

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Plan not found")
                );

        if (!plan.getActive()) {
            throw new ResourceNotFoundException("Plan not found");
        }

        LocalDate today = LocalDate.now();

        Subscription subscription = subscriptionRepository
                .findByUser(user)
                .orElseGet(() ->
                        Subscription.builder()
                                .user(user)
                                .build()
                );

        LocalDate startDate;
        LocalDate endDate;

        if (subscription.getId() != null
                && subscription.getActive()
                && !subscription.getEndDate().isBefore(today)) {

            startDate = subscription.getStartDate();
            endDate = subscription.getEndDate().plusMonths(1);

        } else {

            startDate = today;
            endDate = today.plusMonths(1);
        }

        subscription.setPlan(plan);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setActive(true);

        Subscription savedSubscription =
                subscriptionRepository.save(subscription);

        return mapToResponse(savedSubscription);
    }

    public SubscriptionResponse getMySubscription(String email) {

        User user = getUserByEmail(email);

        Subscription subscription = subscriptionRepository
                .findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription not found"
                        )
                );

        if (subscription.getActive()
                && subscription.getEndDate().isBefore(LocalDate.now())) {

            subscription.setActive(false);
            subscriptionRepository.save(subscription);
        }

        return mapToResponse(subscription);
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private SubscriptionResponse mapToResponse(
            Subscription subscription
    ) {

        Plan plan = subscription.getPlan();

        return new SubscriptionResponse(
                subscription.getId(),
                plan.getId(),
                plan.getName(),
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.getActive(),
                plan.getCustomerLimit(),
                plan.getWhatsappBillingEnabled(),
                plan.getReportsEnabled()
        );
    }

    @Transactional
    public Subscription getActiveSubscription(User user) {

        Subscription subscription = subscriptionRepository
                .findByUser(user)
                .orElseThrow(() ->
                        new AccessDeniedException(
                                "Active subscription required"
                        )
                );

        if (!subscription.getActive()
                || subscription.getEndDate().isBefore(LocalDate.now())) {

            if (subscription.getActive()) {
                subscription.setActive(false);
                subscriptionRepository.save(subscription);
            }

            throw new AccessDeniedException(
                    "Active subscription required"
            );
        }

        return subscription;
    }
}