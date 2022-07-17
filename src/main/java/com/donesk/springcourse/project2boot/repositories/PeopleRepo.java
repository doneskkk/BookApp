package com.donesk.springcourse.project2boot.repositories;


import com.donesk.springcourse.project2boot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PeopleRepo extends JpaRepository<Person, Integer> {
    Optional<Person> findByFullName(String fullName);
}
