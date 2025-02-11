package com.behalf.delta.repo;


import com.behalf.delta.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.travelRequest.id = :requestId AND c.parentComment IS NULL")
    List<Comment> findTopLevelCommentsByTravelRequestId(@Param("requestId") Long requestId);


}
