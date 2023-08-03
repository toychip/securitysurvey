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
            // 질문의 ID를 사용하여 map에서 사용자 응답을 가져옴
            AnswerDto answerDto = answerMap.get(String.valueOf(question.getId()));  // 수정된 부분
            String response = (answerDto != null) ? answerDto.getResponse() : null; // answerDto가 null이면 null값을 넣음
            MultipartFile multipartFile = (answerDto != null) ? answerDto.getFile() : null; // answerDto의 파일을 꺼내서 null이면 null값을 넣음

            // response가 파일일 경우에
            if (multipartFile != null && !multipartFile.isEmpty()) {
                // MultipartFile을 File로 변환
                File file = s3Uploader.convert(multipartFile)
                        .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 변환 실패"));
                // 파일을 업로드하고 URL을 가져오기
                response = s3Uploader.upload(file, "nice-imformation-security");
            }

            // Answer 객체 생성
            Answer answer = Answer.builder()
                    .content(question.getContent())
                    .isRequired(question.getIsRequired())
                    .response(response)
                    .emailname(emailname + "@nicednr.co.kr")
                    .build();

            // Answer 객체를 저장
            answerRepository.save(answer);
        }
    }

    // 이메일 이름이 이미 존재하는지 확인하는 메서드
    public boolean alreadyExistsValidate(String emailname) {
        return answerRepository.existsByEmailname(emailname);
    }
}
