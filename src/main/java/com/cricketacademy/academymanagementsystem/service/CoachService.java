package com.cricketacademy.academymanagementsystem.service;

import com.cricketacademy.academymanagementsystem.dto.CoachResponse;
import com.cricketacademy.academymanagementsystem.dto.CreateCoachRequest;
import com.cricketacademy.academymanagementsystem.entity.Coach;
import com.cricketacademy.academymanagementsystem.entity.Role;
import com.cricketacademy.academymanagementsystem.entity.User;
import com.cricketacademy.academymanagementsystem.repository.CoachRepository;
import com.cricketacademy.academymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoachService {

    private final CoachRepository coachRepository;
    private final UserRepository userRepository;

    public CoachResponse createCoachProfile(CreateCoachRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        if (user.getRole() != Role.COACH) {
            throw new IllegalArgumentException("User must have role COACH to create a coach profile");
        }

        if (coachRepository.findByUserId(user.getId()).isPresent()) {
            throw new IllegalArgumentException("Coach profile already exists for this user");
        }

        Coach coach = Coach.builder()
                .user(user)
                .specialization(request.getSpecialization())
                .bio(request.getBio())
                .build();

        Coach saved = coachRepository.save(coach);

        return new CoachResponse(saved.getId(), user.getUsername(), user.getEmail(),
                saved.getSpecialization(), saved.getBio());
    }
}