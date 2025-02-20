package com.behalf.delta.web;

import com.behalf.delta.entity.Comment;
import com.behalf.delta.entity.TravelRequest;
import com.behalf.delta.repo.CommentRepository;
import com.behalf.delta.repo.TravelRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/travel-requests")
public class TravelRequestController {

    @Autowired
    private TravelRequestRepository travelRequestRepository;

    @Autowired
    private CommentRepository commentRepository;

    // Fetch comments for a travel request

    @GetMapping
    public ResponseEntity<List<TravelRequest>> fetchTravelQuest() {
        return ResponseEntity.ok(travelRequestRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<String> createTravelReq(@RequestBody TravelRequest travelRequest) {
        travelRequestRepository.save(travelRequest);
        return ResponseEntity.ok("Done");
    }
    @GetMapping("/{requestId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long requestId) {
        return ResponseEntity.ok(commentRepository.findByTravelRequestId(requestId));
    }

    // Add a new comment
    @PostMapping("/{requestId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long requestId, @RequestBody Comment comment) {
        Optional<TravelRequest> request = travelRequestRepository.findById(requestId);
        if (request.isPresent()) {
            comment.setTravelRequest(request.get());
            Comment savedComment = commentRepository.save(comment);
            return ResponseEntity.ok(savedComment);
        }
        return ResponseEntity.notFound().build();
    }

    // Reply to a comment
    @PostMapping("/comments/{commentId}/reply")
    public ResponseEntity<Comment> replyToComment(@PathVariable Long commentId, @RequestBody Comment reply) {
        Optional<Comment> parentComment = commentRepository.findById(commentId);
        if (parentComment.isPresent()) {
            reply.setParentComment(parentComment.get());
            reply.setTravelRequest(parentComment.get().getTravelRequest());
            Comment savedReply = commentRepository.save(reply);
            return ResponseEntity.ok(savedReply);
        }
        return ResponseEntity.notFound().build();
    }
}
