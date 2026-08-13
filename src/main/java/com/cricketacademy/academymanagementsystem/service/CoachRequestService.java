package com.cricketacademy.academymanagementsystem.service;
import java.util.List;
import java.time.LocalDateTime;

import com.cricketacademy.academymanagementsystem.repository.CoachRepository;
import org.springframework.transaction.annotation.Transactional;
import com.cricketacademy.academymanagementsystem.dto.CoachRequestResponse;
import com.cricketacademy.academymanagementsystem.dto.SubmitCoachRequestDto;
import com.cricketacademy.academymanagementsystem.entity.*;
import com.cricketacademy.academymanagementsystem.repository.CoachRequestRepository;
import com.cricketacademy.academymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoachRequestService {

    private final CoachRequestRepository coachRequestRepository;
    private final UserRepository userRepository;
    private final CoachRepository coachRepository;

    public CoachRequestResponse submitRequest(String username, SubmitCoachRequestDto dto) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole() != Role.COACH) {
            throw new IllegalArgumentException("Only users with role COACH can submit a coach request");
        }

        if (coachRequestRepository.existsByUser_IdAndStatus(user.getId(), RequestStatus.PENDING)) {
            throw new IllegalArgumentException("You already have a pending coach request");
        }

        CoachRequest request = CoachRequest.builder()
                .user(user)
                .specialization(dto.getSpecialization())
                .bio(dto.getBio())
                .status(RequestStatus.PENDING)
                .requestedAt(LocalDateTime.now())
                .build();

        CoachRequest saved = coachRequestRepository.save(request);

        return new CoachRequestResponse(saved.getId(), user.getUsername(), saved.getSpecialization(),
                saved.getBio(), saved.getStatus(), saved.getRequestedAt());
    }

    public List<CoachRequestResponse> getRequestsByStatus(RequestStatus status) {
        return coachRequestRepository.findByStatus(status).stream()
                .map(r -> new CoachRequestResponse(r.getId(), r.getUser().getUsername(),
                        r.getSpecialization(), r.getBio(), r.getStatus(), r.getRequestedAt()))
                .toList();
    }
    @Transactional
    public CoachRequestResponse approveRequest(Long requestId, String remarks) {

        CoachRequest request = coachRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + requestId));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING requests can be approved");
        }

        User user = request.getUser();

        if (coachRepository.findByUserId(user.getId()).isPresent()) {
            throw new IllegalArgumentException("A coach profile already exists for this user");
        }

        Coach coach = Coach.builder()
                .user(user)
                .specialization(request.getSpecialization())
                .bio(request.getBio())
                .build();
        coachRepository.save(coach);

        request.setStatus(RequestStatus.APPROVED);
        request.setReviewedAt(LocalDateTime.now());
        request.setRemarks(remarks);
        CoachRequest updated = coachRequestRepository.save(request);

        return new CoachRequestResponse(updated.getId(), user.getUsername(), updated.getSpecialization(),
                updated.getBio(), updated.getStatus(), updated.getRequestedAt());
    }

    public CoachRequestResponse rejectRequest(Long requestId, String remarks) {

        CoachRequest request = coachRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + requestId));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING requests can be rejected");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setReviewedAt(LocalDateTime.now());
        request.setRemarks(remarks);
        CoachRequest updated = coachRequestRepository.save(request);

        return new CoachRequestResponse(updated.getId(), request.getUser().getUsername(),
                updated.getSpecialization(), updated.getBio(), updated.getStatus(), updated.getRequestedAt());
    }
}