package com.donesk.springcourse.project2boot.services;



import com.donesk.springcourse.project2boot.models.Book;
import com.donesk.springcourse.project2boot.models.Person;
import com.donesk.springcourse.project2boot.repositories.PeopleRepo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepo peopleRepo;

    @Autowired
    public PeopleService(PeopleRepo peopleRepo) {
        this.peopleRepo = peopleRepo;
    }

    public List<Person> findAll(){
        return peopleRepo.findAll();
    }

    public Person findOne(int id){
        Optional<Person> foundPerson = peopleRepo.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person){
        peopleRepo.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        peopleRepo.save(updatedPerson);
    }

    @Transactional
    public void delete(int id){
        peopleRepo.findById(id).get().getBooks().forEach(book -> {book.setOwner(null);});
        peopleRepo.deleteById(id);
    }

    public Optional<Person> findByFullName(String fullName){
        return peopleRepo.findByFullName(fullName);
    }

    public List<Book> getBooksByPersonId(int id){
        Optional<Person> person = peopleRepo.findById(id);

        if (person.isPresent()){
            Hibernate.initialize(person.get().getBooks());

                 //check book
            person.get().getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());

                 //86400000 = 10 days
                if (diffInMillies > 864000000)
                    book.setExpired(true);
            });
            return person.get().getBooks();

        } else {
                return Collections.emptyList();
        }
    }
}
