package com.cricketacademy.academymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "STUDENT_FEEDBACK")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(name = "feedback_comment", length = 2000, nullable = false)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime submittedAt;
}