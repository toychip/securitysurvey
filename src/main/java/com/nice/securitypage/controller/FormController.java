package com.nice.securitypage.controller;

import com.nice.securitypage.DateConfig;
import com.nice.securitypage.dto.FormDto;
import com.nice.securitypage.service.AnswerService;
import com.nice.securitypage.service.FormService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class FormController {
    private final FormService formService;
    private final AnswerService answerService;

    @GetMapping("/main")
    public String main(Model model, HttpServletRequest request) {
        // 점검 시작일
        getPeriod(model);

        // 점검 날짜 검증
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
    public String addItem(@Validated @ModelAttribute FormDto formDto, BindingResult bindingResult,
                          HttpServletRequest request, Model model, HttpSession session) {
        String clientIP = request.getRemoteAddr(); // ip 정보 가져오기
        String clientBrowser = request.getHeader(HttpHeaders.USER_AGENT);   // 브라우저 정보 가져오기
        String emailname = formDto.getEmailname() + "@nicednr.co.kr";   // 입력한 유저의 이메일 정보 가져오기

        if(answerService.isAlready(emailname)){ // 이미 설문을 했는지
            return "redirect:/alreadyFin";
        }

        if (bindingResult.hasErrors()) {

            // 점검 시작일           // 날짜 검증
            getPeriod(model);

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }   // 에러 해결 완

            // ip와 broswer 정보를 넣은 빈 껍데기 만들기
            formDto = formService.updatedFormDto(formDto, clientIP, clientBrowser);
            model.addAttribute("formdto", formDto);
            return "form/addForm";
        }

        formService.write(formDto, clientIP, clientBrowser);
        session.setAttribute("emailname", formDto.getEmailname());

        return "redirect:/question";
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