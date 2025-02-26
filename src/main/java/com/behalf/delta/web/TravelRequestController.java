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
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long requestId) {
        List<Comment> comments = commentRepository.findTopLevelCommentsByTravelRequestId(requestId); // ✅ Fetch only parent comments
        List<CommentDTO> commentDTOs = comments.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(commentDTOs);
    }


    private CommentDTO convertToDTO(Comment comment) {
        List<CommentDTO> replies = comment.getReplies().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new CommentDTO(comment.getId(), comment.getUsername(), comment.getText(),comment.getCreatedAt(), replies);
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
