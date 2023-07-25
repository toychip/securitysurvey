package com.nice.securitypage.service;

import com.nice.securitypage.dto.AnswerDto;
import com.nice.securitypage.entity.Answer;
import com.nice.securitypage.entity.Question;
import com.nice.securitypage.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionService questionService;

    public List<String> submitForm(List<AnswerDto> answerDtoList, String emailname) {
        List<String> errors = new ArrayList<>();
        List<Question> questions = questionService.findAllQuestions();

        // First, validate all responses
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String response = answerDtoList.get(i).getResponse();

            // If the question is required but the response is null, add an error message
            if (question.getIsRequired() && response == null) {
                errors.add("Response for question ID '" + question.getId() + "' is required");
            }
        }

        // If there are no errors, save all responses
        if (errors.isEmpty()) {
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                String response = answerDtoList.get(i).getResponse();

                Answer builderAnswer = Answer.builder()
                        .content(question.getContent())
                        .isRequired(question.getIsRequired())
                        .response(response)
                        .emailname(emailname)
                        .build();

                answerRepository.save(builderAnswer);
            }
        }

        return errors;
    }
    public boolean isAlready(String emailname) {
        return answerRepository.existsByEmailname(emailname);
    }
}
