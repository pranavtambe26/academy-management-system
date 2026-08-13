package com.cricketacademy.academymanagementsystem.service;

import com.cricketacademy.academymanagementsystem.dto.BatchResponse;
import com.cricketacademy.academymanagementsystem.dto.CreateBatchRequest;
import com.cricketacademy.academymanagementsystem.entity.Batch;
import com.cricketacademy.academymanagementsystem.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final BatchRepository batchRepository;

    public BatchResponse createBatch(CreateBatchRequest request) {
        Batch batch = Batch.builder()
                .name(request.getName())
                .schedule(request.getSchedule())
                .build();

        Batch saved = batchRepository.save(batch);

        return new BatchResponse(saved.getId(), saved.getName(), saved.getSchedule());
    }

    public List<BatchResponse> getAllBatches() {
        return batchRepository.findAll().stream()
                .map(b -> new BatchResponse(b.getId(), b.getName(), b.getSchedule()))
                .toList();
    }

    public BatchResponse updateBatch(Long id, CreateBatchRequest request) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found with id: " + id));

        batch.setName(request.getName());
        batch.setSchedule(request.getSchedule());

        Batch updated = batchRepository.save(batch);

        return new BatchResponse(updated.getId(), updated.getName(), updated.getSchedule());
    }

    public void deleteBatch(Long id) {
        if (!batchRepository.existsById(id)) {
            throw new IllegalArgumentException("Batch not found with id: " + id);
        }
        batchRepository.deleteById(id);
    }
}