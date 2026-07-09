package com.milkkhaata.service;

import com.milkkhaata.dto.response.PlanResponse;
import com.milkkhaata.entities.Plan;
import com.milkkhaata.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    public List<PlanResponse> getActivePlans() {

        return planRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PlanResponse mapToResponse(Plan plan) {

        return new PlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getMonthlyPrice(),
                plan.getCustomerLimit(),
                plan.getWhatsappBillingEnabled(),
                plan.getReportsEnabled()
        );
    }
}