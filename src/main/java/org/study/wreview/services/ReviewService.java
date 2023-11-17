package org.study.wreview.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.wreview.models.Review;
import org.study.wreview.repositories.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    ReviewRepository reviewRepository;
    public List<Review> findAll(){
        return reviewRepository.findAll(Sort.by("timestamp"));
    }

    @Transactional
    public void save(Review review){
        reviewRepository.save(review);
    }
}
