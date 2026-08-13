package com.cricketacademy.academymanagementsystem.repository;

import com.cricketacademy.academymanagementsystem.entity.CoachRequest;
import com.cricketacademy.academymanagementsystem.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CoachRequestRepository extends JpaRepository<CoachRequest, Long> {

    List<CoachRequest> findByStatus(RequestStatus status);

    List<CoachRequest> findByUser_Id(Long userId);

    boolean existsByUser_IdAndStatus(Long userId, RequestStatus status);
}