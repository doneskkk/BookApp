package com.donesk.springcourse.project2boot.repositories;


import com.donesk.springcourse.project2boot.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
    public List<Book> findByTitleStartingWith(String title);
}
