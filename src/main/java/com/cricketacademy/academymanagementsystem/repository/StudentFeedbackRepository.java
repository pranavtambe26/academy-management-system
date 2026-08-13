package com.cricketacademy.academymanagementsystem.repository;

import com.cricketacademy.academymanagementsystem.entity.StudentFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentFeedbackRepository extends JpaRepository<StudentFeedback, Long> {

    List<StudentFeedback> findByCoach_Id(Long coachId);
}