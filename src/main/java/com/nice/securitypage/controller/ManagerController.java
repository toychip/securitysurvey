package com.nice.securitypage.controller;

import com.nice.securitypage.dto.ManagerDto;
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
        boolean success = managerService.login(managerDto);

        // 로그인 실패 시
        if (!success) {
            // 세션에 저장된 실패 횟수를 가져온다.
            Integer failureCount = (Integer) session.getAttribute("failureCount");

            // 만약 세션에 실패 횟수 정보가 없다면, 실패 횟수를 1로 설정한다.
            if (failureCount == null) {
                failureCount = 1;
            } else {
                // 세션에 실패 횟수 정보가 있다면, 실패 횟수를 1 증가시킨다.
                failureCount++;
            }

            // 증가시킨 실패 횟수를 다시 세션에 저장한다.
            session.setAttribute("failureCount", failureCount);

            model.addAttribute("errorMessage", "아이디 혹은 비밀번호가 일치하지 않습니다. 실패 횟수: " + failureCount);
            return "form/loginForm";
        }

        // 로그인 성공 시, 실패 횟수를 초기화한다.
        session.setAttribute("failureCount", 0);

        return "redirect:/"; // 로그인 성공 후 리다이렉트할 페이지 URL 수정
    }
}
