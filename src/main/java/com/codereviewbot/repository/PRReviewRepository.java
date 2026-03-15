package com.codereviewbot.repository;

import com.codereviewbot.model.PRReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PRReviewRepository extends JpaRepository<PRReview, Long> {

}