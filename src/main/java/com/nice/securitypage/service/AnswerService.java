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
                           String emailname) {
        for (int i = 0; i < questionIds.size(); i++) {
            Long questionId = questionIds.get(i);
            String response = responseMap.get("response" + i);
            Question question = questionService.findById(questionId);

//            if (response == null) {
//                System.out.println("Response for questionId " + questionId + " is null");
//            }

            Answer builderAnswer = Answer.builder()
                    .content(question.getContent())
                    .isRequired(question.getIsRequired())
                    .response(response)
                    .emailname(emailname)
                    .build();


            answerRepository.save(builderAnswer);
        }
    }

    public boolean isAlready(String emailname) {
        return answerRepository.existsByEmailname(emailname);
    }
}
