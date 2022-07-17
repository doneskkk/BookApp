package com.donesk.springcourse.project2boot.controllers;


import com.donesk.springcourse.project2boot.models.Book;
import com.donesk.springcourse.project2boot.models.Person;
import com.donesk.springcourse.project2boot.services.BookService;
import com.donesk.springcourse.project2boot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false)Integer page,
                        @RequestParam(value = "books_per_page", required = false)Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear){
        if (page == null || booksPerPage == null)
            model.addAttribute("books", bookService.findAll(sortByYear));
        else
            model.addAttribute("books",bookService.findWithPagination(page, booksPerPage, sortByYear));

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book",bookService.findOne(id));
        Person bookOwner = bookService.getBookOwner(id);

        if (bookOwner != null)
            model.addAttribute("owner",bookOwner);
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/new";
        }

        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id")int id){
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id){
        if (bindingResult.hasErrors()){
            return "books/edit";
        }

        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson){
        bookService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id")int id){
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage(){
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(@RequestParam("query")String query, Model model){
            model.addAttribute("books",bookService.searchByTitle(query));
            return "books/search";
    }
}
