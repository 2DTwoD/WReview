package org.study.wreview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.wreview.models.Caller;

import java.util.Optional;

@Repository
public interface CallerRepository extends JpaRepository<Caller, String> {
    public Optional<Caller> findByUsername(String username);
}
