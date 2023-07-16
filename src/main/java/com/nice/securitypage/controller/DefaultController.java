package com.nice.securitypage.controller;

import com.nice.securitypage.DateConfig;
import com.nice.securitypage.dto.FormDto;
import com.nice.securitypage.entity.Form;
import com.nice.securitypage.repository.DepartmentRepository;
import com.nice.securitypage.repository.FormRepository;
import com.nice.securitypage.service.FormService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DefaultController {
    private final FormService formService;



    @GetMapping("/main")
    public String main(Model model, HttpServletRequest request) {
        // 점검 시작일
        getPeriod(model);
        String outOfDate = formService.isOutOfDate();

        if (outOfDate != null) {
            return outOfDate;
        }

        String clientIP = request.getRemoteAddr(); // ip 정보 가져오기
        String clientBrowser = request.getHeader(HttpHeaders.USER_AGENT);   // 브라우저 정보 가져오기
        FormDto formDto = formService.getFormDto(clientIP, clientBrowser); // ip와 broswer 정보를 넣은 빈 껍데기 만들기
        model.addAttribute("formdto", formDto); // view 렌더링

        return "form/addForm";
    }


    @PostMapping("/main")
    public String addItem(@Valid @ModelAttribute FormDto formDto, BindingResult bindingResult,
                          HttpServletRequest request, Model model) {

        String clientIP = request.getRemoteAddr(); // ip 정보 가져오기
        String clientBrowser = request.getHeader(HttpHeaders.USER_AGENT);   // 브라우저 정보 가져오기

        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            // 점검 시작일
            getPeriod(model);

            model.addAttribute("errors", bindingResult.getAllErrors());
            formDto = formService.getFormDto(clientIP, clientBrowser); // ip와 broswer 정보를 넣은 빈 껍데기 만들기
            model.addAttribute("formdto", formDto);
            return "form/addForm";
        }

        formService.write(formDto, clientIP, clientBrowser);
        return "endPage";
    }

    public void getPeriod(Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate fromDate = LocalDate.parse(DateConfig.FROM_DATE, formatter);
        LocalDate toDate = LocalDate.parse(DateConfig.TO_DATE, formatter);

        model.addAttribute("fromYear", fromDate.getYear());
        model.addAttribute("fromMonth", fromDate.getMonthValue());
        model.addAttribute("fromDay", fromDate.getDayOfMonth());

        model.addAttribute("toYear", toDate.getYear());
        model.addAttribute("toMonth", toDate.getMonthValue());
        model.addAttribute("toDay", toDate.getDayOfMonth());
    }
}
