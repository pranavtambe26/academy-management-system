package com.cricketacademy.academymanagementsystem.service;

import com.cricketacademy.academymanagementsystem.dto.StudentFeedbackAdminResponse;
import com.cricketacademy.academymanagementsystem.dto.SubmitStudentFeedbackDto;
import com.cricketacademy.academymanagementsystem.entity.*;
import com.cricketacademy.academymanagementsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentFeedbackService {

    private final StudentFeedbackRepository feedbackRepository;
    private final StudentRepository studentRepository;
    private final CoachRepository coachRepository;
    private final CoachBatchAssignmentRepository coachBatchAssignmentRepository;
    private final StudentBatchAssignmentRepository studentBatchAssignmentRepository;

    public void submitFeedback(String studentUsername, Long coachId, SubmitStudentFeedbackDto dto) {

        Student student = studentRepository.findByUser_Username(studentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for this user"));

        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new IllegalArgumentException("Coach not found with id: " + coachId));

        StudentFeedback feedback = StudentFeedback.builder()
                .student(student)
                .coach(coach)
                .comment(dto.getComment())
                .submittedAt(LocalDateTime.now())
                .build();

        feedbackRepository.save(feedback);
    }

    public List<StudentFeedbackAdminResponse> getAllFeedbackForCoach(Long coachId) {
        return feedbackRepository.findByCoach_Id(coachId).stream()
                .map(f -> new StudentFeedbackAdminResponse(f.getId(), f.getStudent().getUser().getUsername(),
                        f.getCoach().getUser().getUsername(), f.getComment(), f.getSubmittedAt()))
                .toList();
    }
}