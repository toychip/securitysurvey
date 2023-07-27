package com.nice.securitypage;

import com.nice.securitypage.entity.Question;
import com.nice.securitypage.entity.ResponseType;
import com.nice.securitypage.repository.QuestionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

//@Component
@RequiredArgsConstructor
public class QuestionDataInit {

    private final QuestionRepository questionRepository;

//    @PostConstruct
    public void init(){
        Question question1 = Question.builder()
                .content("PC Filter프로그램 전체실행을 수행한 후 검증된 파일을 제거 또는 암호화 하였습니까?")
                .isRequired(true)
                .type(ResponseType.RADIO)
                .build();

        Question question2 = Question.builder()
                .content("CMOs 및 윈도우 로그인 패스워드는 설정되어 있으며, " +
                        "패스워드는 아래와 같이 회사 권고사항을 준수하고 있습니다.\n" +
                        "영문소문자, 영문대문자,숫자, 특수문자 중 세가지 이상 조합으로 8자이상 이거나, " +
                        "두가지 조합으로 10자이상으로 설정하고 있습니다.")
                .isRequired(false)
                .type(ResponseType.RADIO)
                .build();

        Question question3 = Question.builder()
                .content("Window 및 옹용프로그램은 최신 보안 패치로 적용되어 있습니까?\n")
                .isRequired(true)
                .type(ResponseType.RADIO)
                .build();

        Question question4 = Question.builder()
                .content("window 업데이트 버전이 어떻게 되십니까?")
                .isRequired(true)
                .type(ResponseType.INPUT)
                .build();

        Question question5 = Question.builder()
                .content("개인 캐비넷 키는 오픈공간에 보관하지 않으며, 퇴근시 잠금 상태를 확인합니다.")
                .isRequired(false)
                .type(ResponseType.INPUT)
                .build();

        Question question6 = Question.builder()
                .content("공용 캐비넷 관리담당자는 되근시 잠금 상태를 확인합니다.")
                .isRequired(true)
                .type(ResponseType.RADIO)
                .build();

        List<Question> questions = Arrays.asList(question1, question2, question3, question4, question5, question6);

        // 저장하기
        questionRepository.saveAll(questions);
    }
}

