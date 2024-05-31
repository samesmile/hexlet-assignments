package exercise.controller;

import exercise.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Person;

@RestController
//@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping(path = "/people/{id}")
    public Person show(@PathVariable long id) {
        return personRepository.findById(id).get();
    }

    @GetMapping(path = "/people")
    @ResponseStatus(HttpStatus.OK)
    public List<Person> getPeople() {
        return personRepository.findAll();
    }

    @PostMapping(path = "/people")
    @ResponseStatus(HttpStatus.CREATED)
    public Person createPeople(@RequestBody Person person) {
        personRepository.saveAndFlush(person);

        return person;
    }

    @DeleteMapping(path = "/people/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Long deletePerson(@PathVariable long id) {
        personRepository.deleteById(id);

        return id;
    }
}
