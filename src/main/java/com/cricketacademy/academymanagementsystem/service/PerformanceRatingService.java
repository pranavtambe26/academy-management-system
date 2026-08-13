package com.cricketacademy.academymanagementsystem.service;

import com.cricketacademy.academymanagementsystem.dto.GiveRatingRequest;
import com.cricketacademy.academymanagementsystem.dto.PerformanceRatingResponse;
import com.cricketacademy.academymanagementsystem.entity.*;
import com.cricketacademy.academymanagementsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceRatingService {

    private final PerformanceRatingRepository ratingRepository;
    private final StudentRepository studentRepository;
    private final BatchRepository batchRepository;
    private final CoachRepository coachRepository;
    private final CoachBatchAssignmentRepository coachBatchAssignmentRepository;
    private final StudentBatchAssignmentRepository studentBatchAssignmentRepository;

    public PerformanceRatingResponse giveRating(String coachUsername, GiveRatingRequest request) {

        Coach coach = coachRepository.findByUser_Username(coachUsername)
                .orElseThrow(() -> new IllegalArgumentException("Coach profile not found for this user"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + request.getStudentId()));

        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new IllegalArgumentException("Batch not found with id: " + request.getBatchId()));

        if (!coachBatchAssignmentRepository.existsByCoach_IdAndBatch_Id(coach.getId(), batch.getId())) {
            throw new IllegalArgumentException("You are not assigned to this batch");
        }

        if (!studentBatchAssignmentRepository.existsByStudent_IdAndBatch_Id(student.getId(), batch.getId())) {
            throw new IllegalArgumentException("This student does not belong to this batch");
        }

        PerformanceRating rating = PerformanceRating.builder()
                .student(student)
                .batch(batch)
                .coach(coach)
                .rating(request.getRating())
                .remarks(request.getRemarks())
                .ratedDate(LocalDate.now())
                .build();

        PerformanceRating saved = ratingRepository.save(rating);

        return new PerformanceRatingResponse(saved.getId(), student.getUser().getUsername(), batch.getName(),
                coach.getUser().getUsername(), saved.getRating(), saved.getRemarks(), saved.getRatedDate());
    }

    public List<PerformanceRatingResponse> getMyRatings(String studentUsername) {
        Student student = studentRepository.findByUser_Username(studentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for this user"));

        return ratingRepository.findByStudent_Id(student.getId()).stream()
                .map(r -> new PerformanceRatingResponse(r.getId(), r.getStudent().getUser().getUsername(),
                        r.getBatch().getName(), r.getCoach().getUser().getUsername(),
                        r.getRating(), r.getRemarks(), r.getRatedDate()))
                .toList();
    }
}