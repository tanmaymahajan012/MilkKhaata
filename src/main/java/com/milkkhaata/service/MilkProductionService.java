package com.milkkhaata.service;

import com.milkkhaata.dto.request.MilkProductionRequest;
import com.milkkhaata.dto.response.MilkProductionResponse;
import com.milkkhaata.entities.MilkProductionEntry;
import com.milkkhaata.entities.User;
import com.milkkhaata.exception.ResourceNotFoundException;
import com.milkkhaata.repository.MilkProductionEntryRepository;
import com.milkkhaata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MilkProductionService {

    private final MilkProductionEntryRepository milkProductionEntryRepository;
    private final UserRepository userRepository;

    public MilkProductionResponse createProductionEntry(
            MilkProductionRequest request,
            String email
    ) {

        User user = getUserByEmail(email);

        MilkProductionEntry entry = MilkProductionEntry.builder()
                .entryDate(request.getEntryDate())
                .liters(request.getLiters())
                .ratePerLiter(request.getRatePerLiter())
                .shift(request.getShift())
                .user(user)
                .build();

        MilkProductionEntry savedEntry =
                milkProductionEntryRepository.save(entry);

        return mapToResponse(savedEntry);
    }

    public List<MilkProductionResponse> getAllProductionEntries(
            String email
    ) {

        User user = getUserByEmail(email);

        return milkProductionEntryRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MilkProductionResponse getProductionEntryById(
            Long entryId,
            String email
    ) {

        User user = getUserByEmail(email);

        MilkProductionEntry entry = milkProductionEntryRepository
                .findByIdAndUser(entryId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Milk production entry not found"
                        )
                );

        return mapToResponse(entry);
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private MilkProductionResponse mapToResponse(
            MilkProductionEntry entry
    ) {

        return new MilkProductionResponse(
                entry.getId(),
                entry.getEntryDate(),
                entry.getLiters(),
                entry.getRatePerLiter(),
                entry.getShift(),
                entry.getCreatedAt(),
                entry.getUpdatedAt()
        );
    }

    public MilkProductionResponse updateProductionEntry(
            Long entryId,
            MilkProductionRequest request,
            String email
    ) {

        User user = getUserByEmail(email);

        MilkProductionEntry entry = milkProductionEntryRepository
                .findByIdAndUser(entryId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Milk production entry not found"
                        )
                );

        entry.setEntryDate(request.getEntryDate());
        entry.setLiters(request.getLiters());
        entry.setRatePerLiter(request.getRatePerLiter());
        entry.setShift(request.getShift());

        MilkProductionEntry updatedEntry =
                milkProductionEntryRepository.save(entry);

        return mapToResponse(updatedEntry);
    }

    public void deleteProductionEntry(
            Long entryId,
            String email
    ) {

        User user = getUserByEmail(email);

        MilkProductionEntry entry = milkProductionEntryRepository
                .findByIdAndUser(entryId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Milk production entry not found"
                        )
                );

        milkProductionEntryRepository.delete(entry);
    }
}