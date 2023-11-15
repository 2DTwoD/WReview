package org.study.wreview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.wreview.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
    Optional<Person> findByUsername(String username);
    List<Person> findByIamWorkerTrue();
}
