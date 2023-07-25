package com.nice.securitypage.controller;

import com.nice.securitypage.dto.AnswerDto;
import com.nice.securitypage.entity.Answer;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @GetMapping("/question")
    public String getQuestions(Model model) {

        // if 문으로 emailname 이름이 있느지 확인하고 이미 제출했다는 것 오게하기

        List<Question> questions = questionService.findAllQuestions();
        model.addAttribute("question", questions);
        model.addAttribute("answer", new Answer());
        model.addAttribute("errors", new ArrayList<>()); // Add this line
        return "questionForm";
    }

    @PostMapping("/question")
    public String submitAnswers(
            @ModelAttribute("answerDto") List<AnswerDto> answerDtoList,
            HttpSession session, Model model
    ) {
        String emailname = (String) session.getAttribute("emailname");
        List<String> errors = answerService.submitForm(answerDtoList, emailname);

        if (!errors.isEmpty()) {
            // Handle the errors. For example, add them to the model and return the same form view.
            model.addAttribute("errors", errors);
            model.addAttribute("question", questionService.findAllQuestions());
            model.addAttribute("answer", new Answer());
            return "questionForm";
        }

        return "redirect:/endPage";
    }

    @GetMapping("/endPage")
    public String endPage() {
        return "endPage";
    }

}
