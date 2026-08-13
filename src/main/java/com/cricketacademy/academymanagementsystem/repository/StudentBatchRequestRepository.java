package com.cricketacademy.academymanagementsystem.repository;

import com.cricketacademy.academymanagementsystem.entity.RequestStatus;
import com.cricketacademy.academymanagementsystem.entity.StudentBatchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentBatchRequestRepository extends JpaRepository<StudentBatchRequest, Long> {

    List<StudentBatchRequest> findByStatus(RequestStatus status);

    List<StudentBatchRequest> findByUser_Id(Long userId);

    boolean existsByUser_IdAndBatch_IdAndStatus(Long userId, Long batchId, RequestStatus status);
}