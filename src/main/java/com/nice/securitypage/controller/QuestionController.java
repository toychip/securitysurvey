package com.nice.securitypage.controller;

import com.nice.securitypage.entity.Answer;
import com.nice.securitypage.entity.Question;
import com.nice.securitypage.service.AnswerService;
import com.nice.securitypage.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("answer", new Answer());
        return "questionForm";
    }

    @PostMapping("/question")
    public String endQuestions(@ModelAttribute Answer answer){
        answerService.save(answer);
        return "redirect:/end";
    }
}
