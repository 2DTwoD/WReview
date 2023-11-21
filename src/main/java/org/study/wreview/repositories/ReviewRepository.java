package org.study.wreview.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.wreview.models.Person;
import org.study.wreview.models.Review;

import java.util.List;

@Repository
public interface ReviewRepository  extends JpaRepository<Review, Long> {
    List<Review> findAllByCaller(Person caller, Sort sort);
    List<Review> findAllByWorker(Person caller, Sort sort);
}
