package com.cricketacademy.academymanagementsystem.service;

import com.cricketacademy.academymanagementsystem.dto.StudentBatchRequestResponse;
import com.cricketacademy.academymanagementsystem.dto.SubmitStudentBatchRequestDto;
import com.cricketacademy.academymanagementsystem.entity.*;
import com.cricketacademy.academymanagementsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.time.*;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class StudentBatchRequestService {

    private final StudentBatchRequestRepository studentBatchRequestRepository;
    private final UserRepository userRepository;
    private final BatchRepository batchRepository;
    private final StudentRepository studentRepository;
    private final StudentBatchAssignmentRepository studentBatchAssignmentRepository;

    public StudentBatchRequestResponse submitRequest(String username, SubmitStudentBatchRequestDto dto) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole() != Role.STUDENT) {
            throw new IllegalArgumentException("Only users with role STUDENT can submit a batch join request");
        }

        Batch batch = batchRepository.findById(dto.getBatchId())
                .orElseThrow(() -> new IllegalArgumentException("Batch not found with id: " + dto.getBatchId()));

        if (studentBatchRequestRepository.existsByUser_IdAndBatch_IdAndStatus(
                user.getId(), batch.getId(), RequestStatus.PENDING)) {
            throw new IllegalArgumentException("You already have a pending request for this batch");
        }

        StudentBatchRequest request = StudentBatchRequest.builder()
                .user(user)
                .batch(batch)
                .parentContactNumber(dto.getParentContactNumber())
                .status(RequestStatus.PENDING)
                .requestedAt(LocalDateTime.now())
                .build();

        StudentBatchRequest saved = studentBatchRequestRepository.save(request);

        return new StudentBatchRequestResponse(saved.getId(), user.getUsername(), batch.getName(),
                saved.getParentContactNumber(), saved.getStatus(), saved.getRequestedAt());
    }

    public List<StudentBatchRequestResponse> getRequestsByStatus(RequestStatus status) {
        return studentBatchRequestRepository.findByStatus(status).stream()
                .map(r -> new StudentBatchRequestResponse(r.getId(), r.getUser().getUsername(),
                        r.getBatch().getName(), r.getParentContactNumber(), r.getStatus(), r.getRequestedAt()))
                .toList();
    }

    @Transactional
    public StudentBatchRequestResponse approveRequest(Long requestId, String remarks) {

        StudentBatchRequest request = studentBatchRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + requestId));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING requests can be approved");
        }

        User user = request.getUser();
        Batch batch = request.getBatch();

        Student student = studentRepository.findByUserId(user.getId())
                .orElseGet(() -> studentRepository.save(
                        Student.builder()
                                .user(user)
                                .parentContactNumber(request.getParentContactNumber())
                                .build()
                ));

        if (studentBatchAssignmentRepository.existsByStudent_IdAndBatch_Id(student.getId(), batch.getId())) {
            throw new IllegalArgumentException("This student is already assigned to this batch");
        }

        StudentBatchAssignment assignment = StudentBatchAssignment.builder()
                .student(student)
                .batch(batch)
                .assignedDate(LocalDate.now())
                .build();
        studentBatchAssignmentRepository.save(assignment);

        request.setStatus(RequestStatus.APPROVED);
        request.setReviewedAt(LocalDateTime.now());
        request.setRemarks(remarks);
        StudentBatchRequest updated = studentBatchRequestRepository.save(request);

        return new StudentBatchRequestResponse(updated.getId(), user.getUsername(), batch.getName(),
                updated.getParentContactNumber(), updated.getStatus(), updated.getRequestedAt());
    }

    public StudentBatchRequestResponse rejectRequest(Long requestId, String remarks) {

        StudentBatchRequest request = studentBatchRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + requestId));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING requests can be rejected");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setReviewedAt(LocalDateTime.now());
        request.setRemarks(remarks);
        StudentBatchRequest updated = studentBatchRequestRepository.save(request);

        return new StudentBatchRequestResponse(updated.getId(), request.getUser().getUsername(),
                request.getBatch().getName(), updated.getParentContactNumber(), updated.getStatus(), updated.getRequestedAt());
    }
}