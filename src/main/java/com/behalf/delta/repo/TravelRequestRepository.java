package com.behalf.delta.repo;

import com.behalf.delta.entity.Comment;
import com.behalf.delta.entity.TravelRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRequestRepository extends JpaRepository<TravelRequest, Long> {

}