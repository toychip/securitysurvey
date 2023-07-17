package com.nice.securitypage.service;

import com.nice.securitypage.entity.Answer;
import com.nice.securitypage.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void save(Answer answer) {
        answerRepository.save(answer);
    }
}
