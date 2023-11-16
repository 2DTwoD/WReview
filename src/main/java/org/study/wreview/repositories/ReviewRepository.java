package org.study.wreview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.wreview.models.Review;
@Repository
public interface ReviewRepository  extends JpaRepository<Review, Long> {
}
