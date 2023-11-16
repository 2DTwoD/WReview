package org.study.wreview.services;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.study.wreview.models.PersonWithRating;
import org.study.wreview.repositories.PersonRepository;
import org.study.wreview.repositories.PersonWithRatingRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonWithRatingService {
    PersonWithRatingRepository personWithRatingRepository;

    public List<PersonWithRating> findAll(){
        return personWithRatingRepository.findAllByRatingNotNullOrderByRatingDesc();
    }
}
