package com.behalf.delta.web;

import com.behalf.delta.entity.Comment;
import com.behalf.delta.entity.TravelRequest;
import com.behalf.delta.entity.dto.CommentDTO;
import com.behalf.delta.repo.CommentRepository;
import com.behalf.delta.repo.TravelRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/travel-requests")
public class TravelRequestController {

    @Autowired
    private TravelRequestRepository travelRequestRepository;

    @GetMapping("/fetch")
    public ResponseEntity<List<TravelRequest>> fetchTravelQuest() {
        return ResponseEntity.ok(travelRequestRepository.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTravelReq(@RequestBody TravelRequest travelRequest) {
        travelRequestRepository.save(travelRequest);
        return ResponseEntity.ok("Done");
    }
}
