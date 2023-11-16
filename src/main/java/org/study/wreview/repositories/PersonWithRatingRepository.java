package org.study.wreview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.wreview.models.PersonWithRating;

import java.util.List;

@Repository
public interface PersonWithRatingRepository extends JpaRepository<PersonWithRating, String> {
    List<PersonWithRating> findAllByRatingNotNullOrderByRatingDesc();
}
