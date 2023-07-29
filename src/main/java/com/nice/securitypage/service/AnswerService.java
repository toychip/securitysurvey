package com.nice.securitypage.service;

import com.nice.securitypage.config.S3Uploader;
import com.nice.securitypage.dto.AnswerDto;
import com.nice.securitypage.entity.Answer;
import com.nice.securitypage.entity.Question;
import com.nice.securitypage.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionService questionService;
    private final S3Uploader s3Uploader;

    // 사용자의 답변을 제출하는 메서드
    public void submitForm(Map<String, AnswerDto> answerMap, String emailname) {
        List<Question> questions = questionService.findAllQuestions();

        for(Question question : questions) {
            // Get the user's response from the map using the question's ID
            AnswerDto answerDto = answerMap.get(String.valueOf(question.getId()));  // 수정된 부분
            String response = (answerDto != null) ? answerDto.getResponse() : null;
            MultipartFile multipartFile = (answerDto != null) ? answerDto.getFile() : null;


            if (multipartFile != null && !multipartFile.isEmpty()) {
                // Convert the MultipartFile to File
                File file = s3Uploader.convert(multipartFile)
                        .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File conversion failed"));
                // Upload the file and get the URL
                response = s3Uploader.upload(file, "nice-imformation-security");
            }

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
