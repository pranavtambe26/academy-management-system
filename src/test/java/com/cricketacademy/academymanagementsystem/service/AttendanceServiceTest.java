package com.cricketacademy.academymanagementsystem.service;

import com.cricketacademy.academymanagementsystem.dto.AttendanceResponse;
import com.cricketacademy.academymanagementsystem.dto.MarkAttendanceRequest;
import com.cricketacademy.academymanagementsystem.entity.*;
import com.cricketacademy.academymanagementsystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock private AttendanceRepository attendanceRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private BatchRepository batchRepository;
    @Mock private CoachRepository coachRepository;
    @Mock private CoachBatchAssignmentRepository coachBatchAssignmentRepository;
    @Mock private StudentBatchAssignmentRepository studentBatchAssignmentRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private Coach coach;
    private Student student;
    private Batch batch;
    private MarkAttendanceRequest request;

    @BeforeEach
    void setUp() {
        User coachUser = User.builder().id(1L).username("coach1").role(Role.COACH).build();
        coach = Coach.builder().id(10L).user(coachUser).specialization("Batting").build();

        User studentUser = User.builder().id(2L).username("student1").role(Role.STUDENT).build();
        student = Student.builder().id(20L).user(studentUser).build();

        batch = Batch.builder().id(30L).name("U-14 Batch A").schedule("Mon/Wed/Fri").build();

        request = new MarkAttendanceRequest();
        request.setStudentId(20L);
        request.setBatchId(30L);
        request.setDate(LocalDate.of(2026, 8, 12));
        request.setStatus(AttendanceStatus.PRESENT);
    }

    @Test
    void markAttendance_shouldSucceed_whenCoachIsAssignedToBatchAndStudentBelongsToBatch() {
        // Arrange
        when(coachRepository.findByUser_Username("coach1")).thenReturn(Optional.of(coach));
        when(studentRepository.findById(20L)).thenReturn(Optional.of(student));
        when(batchRepository.findById(30L)).thenReturn(Optional.of(batch));
        when(coachBatchAssignmentRepository.existsByCoach_IdAndBatch_Id(10L, 30L)).thenReturn(true);
        when(studentBatchAssignmentRepository.existsByStudent_IdAndBatch_Id(20L, 30L)).thenReturn(true);
        when(attendanceRepository.findByStudent_IdAndBatch_IdAndDate(20L, 30L, request.getDate()))
                .thenReturn(Optional.empty());

        Attendance savedAttendance = Attendance.builder()
                .id(100L).student(student).batch(batch)
                .date(request.getDate()).status(AttendanceStatus.PRESENT).markedByCoach(coach)
                .build();
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(savedAttendance);

        // Act
        AttendanceResponse response = attendanceService.markAttendance("coach1", request);

        // Assert
        assertNotNull(response);
        assertEquals(AttendanceStatus.PRESENT, response.getStatus());
        assertEquals("student1", response.getStudentUsername());
        verify(attendanceRepository).save(any(Attendance.class));
    }

    @Test
    void markAttendance_shouldThrowException_whenCoachIsNotAssignedToBatch() {
        // Arrange
        when(coachRepository.findByUser_Username("coach1")).thenReturn(Optional.of(coach));
        when(studentRepository.findById(20L)).thenReturn(Optional.of(student));
        when(batchRepository.findById(30L)).thenReturn(Optional.of(batch));
        when(coachBatchAssignmentRepository.existsByCoach_IdAndBatch_Id(10L, 30L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> attendanceService.markAttendance("coach1", request)
        );

        assertEquals("You are not assigned to this batch", exception.getMessage());
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }

    @Test
    void markAttendance_shouldThrowException_whenStudentDoesNotBelongToBatch() {
        // Arrange
        when(coachRepository.findByUser_Username("coach1")).thenReturn(Optional.of(coach));
        when(studentRepository.findById(20L)).thenReturn(Optional.of(student));
        when(batchRepository.findById(30L)).thenReturn(Optional.of(batch));
        when(coachBatchAssignmentRepository.existsByCoach_IdAndBatch_Id(10L, 30L)).thenReturn(true);
        when(studentBatchAssignmentRepository.existsByStudent_IdAndBatch_Id(20L, 30L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> attendanceService.markAttendance("coach1", request));
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }
}