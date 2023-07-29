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
    public String getQuestions(Model model) {

        List<Question> questions = questionService.findAllQuestions();
        model.addAttribute("question", questions);
        model.addAttribute("answerDtoWrapper", new AnswerDtoWrapper());  // 추가된 부분
        return "questionForm";
    }

    @PostMapping("/question")
    public String submitAnswers(
            @ModelAttribute AnswerDtoWrapper answerDtoWrapper,
            HttpSession session
    ) {
        System.out.println("answerDtoWrapper = " + answerDtoWrapper.getAnswerMap().values());
        String emailname = (String) session.getAttribute("emailname");
        answerService.submitForm(answerDtoWrapper.getAnswerMap(), emailname);

        return "redirect:/endPage";
    }

    @GetMapping("/endPage")
    public String endPage() {
        return "endPage";
    }
}
