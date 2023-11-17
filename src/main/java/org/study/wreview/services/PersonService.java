package org.study.wreview.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.wreview.models.Person;
import org.study.wreview.repositories.PersonRepository;
import org.study.wreview.utils.Sorting;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonService {
    PersonRepository personRepository;

    public List<Person> findAll(){
        return personRepository.findAll(Sort.by("username"));
    }

    public List<Person> findWorkers(Sorting sorting){
        List<Person> persons = personRepository.findByIamWorkerTrueOrderByUsername();
        if (sorting == Sorting.RATING_DESC){
            persons.sort((p1, p2) -> (int) (100.0 * (p2.getRating() - p1.getRating())));
        }
        return persons;
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
            p.setEnabled(person.isEnabled());
            personRepository.save(p);
        });
    }

    public Optional<Person> findByUsername(String name){
        return personRepository.findByUsername(name);
    }

    public Optional<Person> findWorkerByUsername(String name){
        return personRepository.findByUsernameAndIamWorkerTrue(name);
    }
}
