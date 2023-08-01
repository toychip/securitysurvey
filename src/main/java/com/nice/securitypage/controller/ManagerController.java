package com.nice.securitypage.controller;

import com.nice.securitypage.dto.AnswerResponse;
import com.nice.securitypage.dto.ChangePasswordDto;
import com.nice.securitypage.dto.ManagerDto;
import com.nice.securitypage.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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


    @GetMapping("/admin")
    public String admin(Model model) {


        List<AnswerResponse> answersResponse = managerService.getAnswers();

        model.addAttribute("answersResponse", answersResponse);
        return "adminPage";
    }

    // 비밀번호 변경 로직

    @GetMapping("/change-password")
    public String getChangePasswordPage(Model model) {
        // 모델에 ChangePasswordDto 객체를 추가하여 타임리프에서 이 객체를 활용하여 폼 데이터를 바인딩
        model.addAttribute("changePasswordDto", new ChangePasswordDto());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(HttpServletRequest request, @Valid @ModelAttribute ChangePasswordDto form, BindingResult result) {
        System.out.println("form.getNewPassword() = " + form.getNewPassword());

//        // 인증 객체가 null이면 로그인 페이지로 리디렉션합니다.
//        if (authentication == null) {
//            return "redirect:/login";
//        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication = " + authentication);

        if (result.hasErrors()) {
            return "change-password";
        }

        // ManagerService의 changePassword 메서드를 호출하여 비밀번호를 변경
        managerService.changePassword(authentication.getName(), form.getNewPassword());

        // 세션 종료 및 로그아웃
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        return "redirect:/login";
    }

    // 스프링 시큐리티를 사용하기 전 세션 로그인 방식

//    @PostMapping("/login")
//    public String login(@Validated @ModelAttribute ManagerDto managerDto,
//                        BindingResult bindingResult, Model model, HttpSession session) {
//        if (bindingResult.hasErrors()) {
//            return "form/loginForm";
//        }
//        boolean success = managerService.login(managerDto, session);
//
//        // 로그인 실패 시
//        if (!success) {
//            String errorMessage;
//            if (managerService.isLocked(managerDto.getUsername())) {
//                errorMessage = "계정이 잠겼습니다.";
//            } else {
//                errorMessage = "아이디 혹은 비밀번호가 일치하지 않습니다.";
//            }
//            model.addAttribute("errorMessage", errorMessage);
//            return "form/loginForm";
//        }
//
//        return "redirect:/admin"; // 로그인 성공 후 리다이렉트할 페이지 URL 수정
//    }
}
