package org.study.wreview.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.wreview.models.Person;
import org.study.wreview.repositories.PersonRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonService {
    @Autowired
    PersonRepository personRepository;

    @Value("${rating.max.workers}")
    int maxSize;

    public List<Person> findAll(){
        return personRepository.findAll(Sort.by("username"));
    }

    public Page<Person> findAllWithFilter(String filter, Pageable pageable){
        return personRepository.findAllWithFilter(filter, pageable);
    }

    public Page<Person> findWorkersWithFilter(String filter, Pageable pageable){
        return personRepository.findWorkersWithFilter(filter, pageable);
    }

    public List<Person> findWorkersWithRating(){
        return personRepository.findByIamWorkerTrueOrderByUsername()
                .stream()
                .sorted(Comparator.comparing(Person::getRating)
                        .thenComparing(Person::getNumOfCalcReviews).reversed()
                        .thenComparing(Person::getUsername))
                .limit(maxSize)
                .toList();
    }
    public List<Person> findWorkers(){
        return personRepository.findByIamWorkerTrueOrderByUsername();
    }

    @Transactional
    public void save(Person person){
        personRepository.save(person);
    }

    @Transactional
    public void update(String name, Person person){
        personRepository.findByUsername(name).ifPresent(p -> {
            p.setBirthday(person.getBirthday());
            p.setPhone(person.getPhone());
            p.setIamWorker(person.isIamWorker());
            p.setServiceDescription(person.getServiceDescription());
            p.setExperienceDate(person.getExperienceDate());
            p.setPrice(person.getPrice());
            personRepository.save(p);
        });
    }

    @Transactional
    public void delete(Person person){
        personRepository.delete(person);
    }

    public Optional<Person> findByUsername(String name){
        return personRepository.findByUsername(name);
    }

    public Optional<Person> findWorkerByUsername(String name){
        return personRepository.findByUsernameAndIamWorkerTrue(name);
    }
}
