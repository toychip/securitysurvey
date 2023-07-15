package com.nice.securitypage.controller;

import com.nice.securitypage.dto.FormDto;
import com.nice.securitypage.entity.Form;
import com.nice.securitypage.repository.DepartmentRepository;
import com.nice.securitypage.repository.FormRepository;
import com.nice.securitypage.service.FormService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DefaultController {

    private final DepartmentRepository departmentRepository;
//    private final FormRepository formRepository;
    private final FormService formService;

    @GetMapping("/main")
    public String main(Model model) {
        String formdto = "formdto";
        model.addAttribute(formdto, new FormDto());
        return "addForm";
    }

    @PostMapping("/main")
    public String addItem(@ModelAttribute FormDto formDto) {

        formService.write(formDto);
        return "endPage";
    }
}
