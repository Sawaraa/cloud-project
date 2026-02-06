package org.example.task.repository;


import org.example.task.data.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.example.task.data.Email;

import java.util.List;

@Repository
public interface EmailRepository extends CrudRepository<Email, String> {

    List<Email> findByStatus(Status status);

}
