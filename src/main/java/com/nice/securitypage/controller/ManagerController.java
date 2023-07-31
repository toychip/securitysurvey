package com.nice.securitypage.controller;

import com.nice.securitypage.dto.ManagerDto;
import com.nice.securitypage.entity.Answer;
import com.nice.securitypage.service.ManagerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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
    public String login(@Validated @ModelAttribute ManagerDto managerDto,
                        BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "form/loginForm";
        }
        boolean success = managerService.login(managerDto, session);

        // 로그인 실패 시
        if (!success) {
            String errorMessage;
            if (managerService.isLocked(managerDto.getUsername())) {
                errorMessage = "계정이 잠겼습니다.";
            } else {
                errorMessage = "아이디 혹은 비밀번호가 일치하지 않습니다.";
            }
            model.addAttribute("errorMessage", errorMessage);
            return "form/loginForm";
        }

        return "redirect:/admin"; // 로그인 성공 후 리다이렉트할 페이지 URL 수정
    }

    @GetMapping("/admin")
    public String admin(Model model) {


        List<Answer> answers = managerService.getAnswers();
        model.addAttribute("answers", answers);
        return "adminPage";
    }
}
