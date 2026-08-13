package com.cricketacademy.academymanagementsystem.service;

import com.cricketacademy.academymanagementsystem.dto.CoachFeedbackResponse;
import com.cricketacademy.academymanagementsystem.dto.SubmitCoachFeedbackDto;
import com.cricketacademy.academymanagementsystem.entity.*;
import com.cricketacademy.academymanagementsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachFeedbackService {

    private final CoachFeedbackRepository feedbackRepository;
    private final StudentRepository studentRepository;
    private final CoachRepository coachRepository;

    public CoachFeedbackResponse submitFeedback(String coachUsername, Long studentId, SubmitCoachFeedbackDto dto) {

        Coach coach = coachRepository.findByUser_Username(coachUsername)
                .orElseThrow(() -> new IllegalArgumentException("Coach profile not found for this user"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        CoachFeedback feedback = CoachFeedback.builder()
                .student(student)
                .coach(coach)
                .comment(dto.getComment())
                .visibility(dto.getVisibility())
                .submittedAt(LocalDateTime.now())
                .build();

        CoachFeedback saved = feedbackRepository.save(feedback);

        return new CoachFeedbackResponse(saved.getId(), coach.getUser().getUsername(),
                saved.getComment(), saved.getVisibility(), saved.getSubmittedAt());
    }

    public List<CoachFeedbackResponse> getMyFeedback(String studentUsername) {
        Student student = studentRepository.findByUser_Username(studentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for this user"));

        return feedbackRepository.findByStudent_IdAndVisibility(student.getId(), FeedbackVisibility.STUDENT_VISIBLE)
                .stream()
                .map(f -> new CoachFeedbackResponse(f.getId(), f.getCoach().getUser().getUsername(),
                        f.getComment(), f.getVisibility(), f.getSubmittedAt()))
                .toList();
    }

    public List<CoachFeedbackResponse> getAllFeedbackForStudent(Long studentId) {
        return feedbackRepository.findByStudent_Id(studentId).stream()
                .map(f -> new CoachFeedbackResponse(f.getId(), f.getCoach().getUser().getUsername(),
                        f.getComment(), f.getVisibility(), f.getSubmittedAt()))
                .toList();
    }
}