package org.study.wreview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.wreview.models.Worker;
@Repository
public interface WorkerRepository extends JpaRepository<Worker, String> {
}
