package com.nice.securitypage.controller;

import com.nice.securitypage.dto.FormDto;
import com.nice.securitypage.entity.Form;
import com.nice.securitypage.repository.DepartmentRepository;
import com.nice.securitypage.repository.FormRepository;
import com.nice.securitypage.service.FormService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DefaultController {
    private final FormService formService;

    @GetMapping("/main")
    public String main(Model model, HttpServletRequest request) {
        String celientIP = request.getRemoteAddr(); // ip 정보 가져오기
        String clientBrowser = request.getHeader(HttpHeaders.USER_AGENT);   // 브라우저 정보 가져오기

        FormDto formDto = getFormDto(celientIP, clientBrowser); // ip와 broswer 정보를 넣은 빈 껍데기 만들기

        model.addAttribute("formdto", formDto); // view 렌더링
        return "addForm";
    }

    // ip와 browser 정보를 넣은 빈 껍데기 만들기
    private static FormDto getFormDto(String celientIP, String clientBrowser) {
        return FormDto.builder()
                .ip(celientIP)
                .browser(clientBrowser)
                .build();
    }

    @PostMapping("/main")
    public String addItem(@ModelAttribute FormDto formDto) {
        formService.write(formDto);
        return "endPage";
    }
}
