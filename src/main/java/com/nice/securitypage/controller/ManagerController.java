package com.nice.securitypage.controller;

import com.nice.securitypage.dto.ManagerDto;
import com.nice.securitypage.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/login")
    public String loginForm(Model model) {

        model.addAttribute("managerDto", new ManagerDto());
        return "form/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute ManagerDto managerDto) {

        managerService.login(managerDto);
        return "redirect:/"; // 로그인 성공 후 리다이렉트할 페이지 URL 수정
    }
}
