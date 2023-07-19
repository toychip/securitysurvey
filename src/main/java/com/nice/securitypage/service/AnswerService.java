package com.nice.securitypage.service;

import com.nice.securitypage.entity.Answer;
import com.nice.securitypage.entity.Question;
import com.nice.securitypage.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionService questionService;

    public void submitForm(List<Long> questionIds,
                           Map<String, String> responseMap,
                           String emailname
                           ) {
        for (Long questionId : questionIds) {
            String response = responseMap.get("response" + questionId);

            // Find the question from the database
            Question question = questionService.findById(questionId);

            Answer builderAnswer = Answer.builder()
                    .content(question.getContent())
                    .isRequired(question.getIsRequired())
                    .response(response)
                    .emailname(emailname)
                    .build();

            answerRepository.save(builderAnswer);
        }
    }
}
