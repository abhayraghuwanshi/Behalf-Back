package com.behalf.delta.repo;


import com.behalf.delta.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTravelRequestId(Long travelRequestId);
}
