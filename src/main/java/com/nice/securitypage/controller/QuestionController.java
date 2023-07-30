package com.nice.securitypage.controller;

import com.nice.securitypage.dto.AnswerDtoWrapper;
import com.nice.securitypage.entity.Question;
import com.nice.securitypage.service.AnswerService;
import com.nice.securitypage.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @GetMapping("/question")
    public String getQuestions(Model model, HttpSession session) {

        // form을 입력하지 않고 url에 question을 직접 입력하고 들어온 경우
        var emailname = session.getAttribute("emailname");
        if(emailname == null){
            return "redirect:/main";
        }

        // 질문을 DB에서 동적으로 갖고옴
        List<Question> questions = questionService.findAllQuestions();
        // 질문지 렌더링
        model.addAttribute("question", questions);
        // 타임리프는 리스트를 받을 수 없으므로 answer를 Wrapper로 감싸서 렌더링
        model.addAttribute("answerDtoWrapper", new AnswerDtoWrapper());
        return "questionForm";
    }

    @PostMapping("/question")
    public String submitAnswers(
            @ModelAttribute AnswerDtoWrapper answerDtoWrapper,
            HttpSession session
    ) {
        // 세션을 통해 사용자 이메일 정보를 가져옴
        String emailname = (String) session.getAttribute("emailname");
        answerService.submitForm(answerDtoWrapper.getAnswerMap(), emailname);

        return "redirect:/endPage";
    }

    @GetMapping("/endPage")
    public String endPage() {
        return "endPage";
    }
}
