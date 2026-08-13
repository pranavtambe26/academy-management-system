package com.cricketacademy.academymanagementsystem.service;

import com.cricketacademy.academymanagementsystem.dto.CreateStudentRequest;
import com.cricketacademy.academymanagementsystem.dto.StudentResponse;
import com.cricketacademy.academymanagementsystem.entity.Role;
import com.cricketacademy.academymanagementsystem.entity.Student;
import com.cricketacademy.academymanagementsystem.entity.User;
import com.cricketacademy.academymanagementsystem.repository.StudentRepository;
import com.cricketacademy.academymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentResponse createStudentProfile(CreateStudentRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        if (user.getRole() != Role.STUDENT) {
            throw new IllegalArgumentException("User must have role STUDENT to create a student profile");
        }

        if (studentRepository.findByUserId(user.getId()).isPresent()) {
            throw new IllegalArgumentException("Student profile already exists for this user");
        }

        Student student = Student.builder()
                .user(user)
                .parentContactNumber(request.getParentContactNumber())
                .build();

        Student saved = studentRepository.save(student);

        return new StudentResponse(saved.getId(), user.getUsername(), user.getEmail(), saved.getParentContactNumber());
    }
}