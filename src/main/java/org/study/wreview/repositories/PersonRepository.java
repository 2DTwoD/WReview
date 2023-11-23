package org.study.wreview.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.study.wreview.models.Person;
import org.study.wreview.models.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {

    Optional<Person> findByUsername(String username);

    Optional<Person> findByUsernameAndIamWorkerTrue(String username);

    List<Person> findByIamWorkerTrueOrderByUsername();

    @Query(value = "select * from users" +
            " where username ~* :filter or phone ~* :filter or service_description ~* :filter",
            nativeQuery = true)
    Page<Person> findAllWithFilter(@Param("filter") String filter, Pageable pageable);

    @Query(value = "select * from users" +
            " where i_am_worker = true and (username ~* :filter or phone ~* :filter or service_description ~* :filter)",
            nativeQuery = true)
    Page<Person> findWorkersWithFilter(@Param("filter") String filter, Pageable pageable);
}
