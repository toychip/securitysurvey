package com.nice.securitypage.controller;

import com.nice.securitypage.dto.FormDto;
import com.nice.securitypage.entity.Organization;
import com.nice.securitypage.service.AnswerService;
import com.nice.securitypage.service.FormService;
import com.nice.securitypage.service.OrganizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class FormController {
    // 개인정보 서비스
    private final FormService formService;
    // 설문조사 서비스
    private final AnswerService answerService;
    // 부서 정보 서비스
    private final OrganizationService organizationService;

    @GetMapping("/main")
    public String main(Model model, HttpServletRequest request) {

        // 점검 날짜 가져오기
        formService.getPeriod(model);

        // 점검 날짜 검증
        String outOfDate = formService.isOutOfDate();
        if (outOfDate != null) {
            return outOfDate;
        }

        Map<Organization, Integer> organizations = organizationService.getOrganization();
        model.addAttribute("organizations", organizations);

        // ip 정보 가져오기
        String clientIP = request.getRemoteAddr();
        // 브라우저 정보 가져오기
        String clientBrowser = request.getHeader(HttpHeaders.USER_AGENT);
        // ip와 broswer 정보를 넣은 입력할 수 있는 빈 껍데기 만들기
        FormDto formDto = formService.getFormDto(clientIP, clientBrowser);
        model.addAttribute("formdto", formDto); // view 렌더링
        return "form/addForm";
    }

    @PostMapping("/main")
    public String addItem(@Validated @ModelAttribute FormDto formDto, BindingResult bindingResult,
                          HttpServletRequest request, Model model, HttpSession session) {
        // ip, 브라우저 정보 가져오기
        String clientIP = request.getRemoteAddr();
        String clientBrowser = request.getHeader(HttpHeaders.USER_AGENT);
        // 입력한 유저의 이메일 정보 가져오기
        String emailname = formDto.getEmailname() + "@nicednr.co.kr";

        // 이미 설문을 완료 했다면
        if(answerService.alreadyExistsValidate(emailname)){
            return "redirect:/alreadyFin";  // 완료한 창으로
        }

        // 입력한 폼이 오류가 있다면
        if (bindingResult.hasErrors()) {
            // 점검 날짜 가져오기
            formService.getPeriod(model);

            // 필드 오류들을 필드에러 형태의 리스트로 생성
            List<FieldError> errors = bindingResult.getFieldErrors();

            // 각 오류들을 기입
            for (FieldError error : errors) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }

            // 사용자가 입력하여 검증이 통과한 필드와 ip와 broswer 정보를 넣은 폼으로 교체 후 렌더링
            formDto = formService.updatedFormDto(formDto, clientIP, clientBrowser);
            model.addAttribute("formdto", formDto);
            return "form/addForm";
        }

        // 검증이 통과한 경우

        // Form DB에 존재하고, Answer DB에는 존재하지 않는 경우에는 데이터를 저장하지 않고 question으로 return
        // Form DB와 ANswer DB에 둘 다 존재하지 않는 경우에만 데이터 추가
        if(!formService.alreadyExistsValidate(emailname)){
            // 데이터 저장
            formService.write(formDto, clientIP, clientBrowser);
        }

        // 이메일을 세션을 통해 저장
        session.setAttribute("emailname", formDto.getEmailname());

        return "redirect:/question";
    }

    // 이미 완료한 폼 렌더링
    @GetMapping("/alreadyFin")
    public String alreadyFinished() {
        return "alreadyFin";
    }

}