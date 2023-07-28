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

    // 사용자의 답변을 제출하는 메서드
    public void submitForm(Map<String, AnswerDto> answerMap, String emailname) {
        List<Question> questions = questionService.findAllQuestions();

        for(Question question : questions) {
            // Get the user's response from the map using the question's ID
            AnswerDto answerDto = answerMap.get(String.valueOf(question.getId()));  // 수정된 부분
            String response = (answerDto != null) ? answerDto.getResponse() : null;

            // Create an Answer object
            System.out.println("question = " + question);
            System.out.println("response = " + response);
            Answer answer = Answer.builder()
                    .content(question.getContent())
                    .isRequired(question.getIsRequired())
                    .response(response)
                    .emailname(emailname + "@nicednr.co.kr")
                    .build();

            // Save the Answer object
            answerRepository.save(answer);
        }
    }

    // 이메일 이름이 이미 존재하는지 확인하는 메서드
    public boolean isAlready(String emailname) {
        return answerRepository.existsByEmailname(emailname);
    }
}
