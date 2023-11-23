package org.study.wreview.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.study.wreview.models.Review;

@Repository
public interface ReviewRepository  extends JpaRepository<Review, Long> {
    @Query(value = "select * from Reviews" +
            " where worker ~* :filter or caller ~* :filter or equipment ~* :filter or reason ~* :filter",
            nativeQuery = true)
    Page<Review> findReviewsWithFilter(@Param("filter") String filter,
                                       Pageable pageable);
    @Query(value = "select * from Reviews" +
            " where caller = :caller and (worker ~* :filter or caller ~* :filter or equipment ~* :filter or reason ~* :filter)",
            nativeQuery = true)
    Page<Review> findAllByCaller(@Param("caller") String caller,
                                 @Param("filter") String filter,
                                 Pageable pageable);
    @Query(value = "select * from Reviews" +
            " where worker = :worker and (worker ~* :filter or caller ~* :filter or equipment ~* :filter or reason ~* :filter)",
            nativeQuery = true)
    Page<Review> findAllByWorker(@Param("worker") String worker,
                                 @Param("filter") String filter,
                                 Pageable pageable);

}
