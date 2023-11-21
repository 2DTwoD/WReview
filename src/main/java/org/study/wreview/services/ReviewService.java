package org.study.wreview.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.wreview.models.Person;
import org.study.wreview.models.Review;
import org.study.wreview.repositories.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    ReviewRepository reviewRepository;
    public Page<Review> findAll(Pageable pages){
        return reviewRepository.findAll(pages);
    }

    public List<Review> findAllByCaller(Person caller){
        return reviewRepository.findAllByCaller(caller, Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    public List<Review> findAllByWorker(Person worker){
        return reviewRepository.findAllByWorker(worker, Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    @Transactional
    public void save(Review review){
        reviewRepository.save(review);
    }
}
