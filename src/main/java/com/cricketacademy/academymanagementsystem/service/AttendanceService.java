package com.cricketacademy.academymanagementsystem.service;

import com.cricketacademy.academymanagementsystem.dto.AttendanceResponse;
import com.cricketacademy.academymanagementsystem.dto.MarkAttendanceRequest;
import com.cricketacademy.academymanagementsystem.entity.*;
import com.cricketacademy.academymanagementsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final BatchRepository batchRepository;
    private final CoachRepository coachRepository;
    private final CoachBatchAssignmentRepository coachBatchAssignmentRepository;
    private final StudentBatchAssignmentRepository studentBatchAssignmentRepository;

    public AttendanceResponse markAttendance(String coachUsername, MarkAttendanceRequest request) {

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

        Attendance attendance = attendanceRepository
                .findByStudent_IdAndBatch_IdAndDate(student.getId(), batch.getId(), request.getDate())
                .map(existing -> {
                    existing.setStatus(request.getStatus());
                    existing.setMarkedByCoach(coach);
                    return existing;
                })
                .orElseGet(() -> Attendance.builder()
                        .student(student)
                        .batch(batch)
                        .date(request.getDate())
                        .status(request.getStatus())
                        .markedByCoach(coach)
                        .build());

        Attendance saved = attendanceRepository.save(attendance);

        return new AttendanceResponse(saved.getId(), student.getUser().getUsername(), batch.getName(),
                saved.getDate(), saved.getStatus(), coach.getUser().getUsername());
    }

    public List<AttendanceResponse> getBatchAttendanceForDate(Long batchId, java.time.LocalDate date) {
        return attendanceRepository.findByBatch_IdAndDate(batchId, date).stream()
                .map(a -> new AttendanceResponse(a.getId(), a.getStudent().getUser().getUsername(),
                        a.getBatch().getName(), a.getDate(), a.getStatus(), a.getMarkedByCoach().getUser().getUsername()))
                .toList();
    }

    public List<AttendanceResponse> getMyAttendance(String studentUsername) {
        Student student = studentRepository.findByUser_Username(studentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for this user"));

        return attendanceRepository.findByStudent_Id(student.getId()).stream()
                .map(a -> new AttendanceResponse(a.getId(), a.getStudent().getUser().getUsername(),
                        a.getBatch().getName(), a.getDate(), a.getStatus(), a.getMarkedByCoach().getUser().getUsername()))
                .toList();
    }
}