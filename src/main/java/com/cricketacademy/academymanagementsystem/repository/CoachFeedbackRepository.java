package com.cricketacademy.academymanagementsystem.repository;

import com.cricketacademy.academymanagementsystem.entity.CoachFeedback;
import com.cricketacademy.academymanagementsystem.entity.FeedbackVisibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoachFeedbackRepository extends JpaRepository<CoachFeedback, Long> {

    List<CoachFeedback> findByStudent_Id(Long studentId);

    List<CoachFeedback> findByStudent_IdAndVisibility(Long studentId, FeedbackVisibility visibility);
}