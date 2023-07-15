package com.nice.securitypage.controller;

import com.nice.securitypage.dto.FormDto;
import com.nice.securitypage.entity.Form;
import com.nice.securitypage.repository.DepartmentRepository;
import com.nice.securitypage.repository.FormRepository;
import com.nice.securitypage.service.FormService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DefaultController {
    private final FormService formService;

    @GetMapping("/main")
    public String main(Model model, HttpServletRequest request) {
        String clientIP = request.getRemoteAddr(); // ip 정보 가져오기
        String clientBrowser = request.getHeader(HttpHeaders.USER_AGENT);   // 브라우저 정보 가져오기

        FormDto formDto = getFormDto(clientIP, clientBrowser); // ip와 broswer 정보를 넣은 빈 껍데기 만들기

        model.addAttribute("formdto", formDto); // view 렌더링
        return "form/addForm";
    }

    // ip와 browser 정보를 넣은 빈 껍데기 만들기
    private static FormDto getFormDto(String clientIP, String clientBrowser) {
        FormDto formDto = FormDto.builder()
                .ip(clientIP)
                .browser(clientBrowser)
                .build();
        return formDto;
    }


    @PostMapping("/main")
    public String addItem(@Validated @ModelAttribute FormDto formDto, BindingResult bindingResult,
                          HttpServletRequest request, Model model) {

        String clientIP = request.getRemoteAddr(); // ip 정보 가져오기
        String clientBrowser = request.getHeader(HttpHeaders.USER_AGENT);   // 브라우저 정보 가져오기

        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            formDto = getFormDto(clientIP, clientBrowser); // ip와 broswer 정보를 넣은 빈 껍데기 만들기
//            redirectAttributes.addFlashAttribute("formdto", formDto);
//            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formdto", bindingResult);
//            model.addAttribute("ip", clientIP);
//            model.addAttribute("browser", clientBrowser);
            model.addAttribute("formdto", formDto); // view 렌더링

            return "form/addForm";
        }

        formService.write(formDto);
        return "endPage";
    }
}
