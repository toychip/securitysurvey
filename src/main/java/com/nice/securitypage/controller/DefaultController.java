package com.nice.securitypage.controller;

import com.nice.securitypage.entity.Department;
import com.nice.securitypage.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DefaultController {

    private final DepartmentRepository repository;

    @GetMapping("/")
    public String main(Model model) {

//        Iterable<Department> departments = DepartmentRepository.findAll();

        model.addAttribute("");
        return "";
    }
}
