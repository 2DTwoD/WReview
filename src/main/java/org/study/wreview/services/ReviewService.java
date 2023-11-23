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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    ReviewRepository reviewRepository;

    public Page<Review> findAll(Pageable pageable){
        return reviewRepository.findAll(pageable);
    }

    public Page<Review> findAllByCaller(String caller, String filter, Pageable pageable){
        return reviewRepository.findAllByCaller(caller, filter, pageable);
    }

    public Page<Review> findAllByWorker(String worker, String filter, Pageable pageable){
        return reviewRepository.findAllByWorker(worker, filter, pageable);
    }

    public Page<Review> findAllWithFilter(String filter, Pageable pageable){
        return reviewRepository.findReviewsWithFilter(filter, pageable);
    }

    public Optional<Review> findById(long id){
        return reviewRepository.findById(id);
    }

    @Transactional
    public void save(Review review){
        reviewRepository.save(review);
    }

    @Transactional
    public void update(long id, Review review){
        reviewRepository.findById(id).ifPresent(r -> {
            r.setEquipment(review.getEquipment());
            r.setReason(review.getReason());
            r.setWorker(review.getWorker());
            r.setWorkDone(review.isWorkDone());
            r.setComment(review.getComment());
            r.setRating(review.getRating());
            reviewRepository.save(r);
        });
    }
    @Transactional
    public void delete(Review review){
        reviewRepository.delete(review);
    }
}
