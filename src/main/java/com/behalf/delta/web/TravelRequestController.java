package com.behalf.delta.web;

import com.behalf.delta.entity.TravelRequest;
import com.behalf.delta.repo.TravelRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travel-requests")
@CrossOrigin(origins = "http://localhost:3000")  // Adjust for frontend URL
public class TravelRequestController {

    @Autowired
    private TravelRequestRepository repository;

    @PostMapping
    public TravelRequest createTravelRequest(@RequestBody TravelRequest request) {
        return repository.save(request);
    }

    @GetMapping
    public List<TravelRequest> getAllTravelRequests() {
        return repository.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteTravelRequest(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
