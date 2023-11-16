package org.study.wreview.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.wreview.models.Person;
import org.study.wreview.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonService {
    final private PersonRepository personRepository;

    public List<Person> findAll(){
        return personRepository.findAll(Sort.by("username"));
    }
    public List<Person> findWorkers(){
        return personRepository.findByIamWorkerTrue();
    }

    public void save(Person person){
        personRepository.save(person);
    }

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
