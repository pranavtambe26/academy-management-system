package com.cricketacademy.academymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "STUDENT_BATCH_ASSIGNMENTS", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "batch_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentBatchAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @Column(nullable = false)
    private LocalDate assignedDate;
}