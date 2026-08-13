package com.cricketacademy.academymanagementsystem.repository;

import com.cricketacademy.academymanagementsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUser_Username(String username);

    Optional<Student> findByUserId(Long userId);
}