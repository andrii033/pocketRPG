package com.ta.pocketRPG.controllers;

import com.ta.pocketRPG.PersonRepository;
import com.ta.pocketRPG.data.LoginForm;
import com.ta.pocketRPG.data.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm, Model model) {
        String name = loginForm.getName();
        String password = loginForm.getPassword();


        Person person = personRepository.findByNameAndPassword(name, password);

        if (person != null) {
            // Successful login, redirect to a welcome page or dashboard
            model.addAttribute("person", person);
            return "welcome";
        } else {
            // Failed login, show login form with an error message
            model.addAttribute("loginError", "Invalid credentials");
            return "login";
        }
    }

    @GetMapping
    public String getAllPersons(Model model) {
        List<Person> persons = personRepository.findAll();
        model.addAttribute("persons", persons);
        return "personList";
    }

    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("person", new Person());
        System.out.println("getMapping----------------------");
        return "register";
    }


    @PostMapping("/register")
    public String registerPerson(@ModelAttribute Person person) {
        personRepository.save(person);
        return "redirect:/persons";
    }

    @GetMapping("/{id}")
    public String getPersonById(@PathVariable Long id, Model model) {
        Person person = personRepository.findById(id).orElse(null);
        model.addAttribute("person", person);
        return "personDetails";
    }

}