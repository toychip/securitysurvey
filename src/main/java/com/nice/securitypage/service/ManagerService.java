package com.nice.securitypage.service;

import com.nice.securitypage.config.AESUtilConfig;
import com.nice.securitypage.config.security.EncryptionUtil;
import com.nice.securitypage.dto.AnswerResponse;
import com.nice.securitypage.entity.Answer;
import com.nice.securitypage.entity.Manager;
import com.nice.securitypage.repository.AnswerRepository;
import com.nice.securitypage.repository.ManagerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService implements UserDetailsService {

    private final ManagerRepository managerRepository;
    private final AnswerRepository answerRepository;
    private final AESUtilConfig aesUtilConfig;

    // DB에서 모든 답변을 가져오고, 그에 대한 응답을 생성하여 반환하는 메서드
    public List<AnswerResponse> getAnswers() {
        // DB에서 모든 답변을 조회
        List<Answer> answers = answerRepository.findAll();
        List<AnswerResponse> answerResponses = new ArrayList<>();

        // 각 답변에 대해
        for (Answer answer : answers) {
            long num = answer.getId() % 6 == 0 ? 6 : answer.getId() % 6; // 질문 번호 1~6으로 나타내기
            // AnswerResponse를 빌드하기 위한 빌더를 초기화
            AnswerResponse.AnswerResponseBuilder answerResponseBuilder = AnswerResponse.builder()
                    .id(num)
                    .content(answer.getContent())
                    .isRequired(answer.getIsRequired())
                    .emailName(answer.getEmailname())
                    .response(answer.getResponse() != null ? answer.getResponse() : null);

            // S3 버킷의 URL
            String url = "https://nicednr-project.s3.ap-northeast-2.amazonaws.com/nice-imformation-security/";

            // 답변이 URL로 시작하면, 이를 복호화
            if (answer.getResponse() != null && answer.getResponse().startsWith(url)) {
                // url절사
                String encryptedPart = answer.getResponse().replace(url, "");
                // 암호화된 이름 복호화
                String decryptedPart = aesUtilConfig.decrypt(encryptedPart);
                // 복호화한 이름으로 응답에 담음
                answerResponseBuilder.decryptedResponse(decryptedPart);
            } else {
                answerResponseBuilder.decryptedResponse(null);
            }
            // AnswerResponse 객체를 빌드하여 리스트에 추가
            answerResponses.add(answerResponseBuilder.build());
        }

        return answerResponses;
    }


    @Override
    // 주어진 username에 해당하는 UserDetails 객체를 반환하는 메서드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = managerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(manager.getUsername())
                .password(manager.getPassword())
                .build();
    }

    // 비밀번호를 바꾸는 메서드
    @Transactional
    public void changePassword(String username, String newPassword) {
        System.out.println("ManagerService.changePassword");
        Manager manager = managerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 입력된 비밀번호를 암호화
        String md5Password = EncryptionUtil.encryptMD5(newPassword);
        String sha256Password = EncryptionUtil.encryptSHA256(md5Password);

        Manager updatedManager = manager.toBuilder()
                .password(sha256Password)
                .createdDate(LocalDateTime.now())
                .build();
        // 새로운 Manager 객체를 저장

        // dirty checking이 일어나므로 이전 객체를 삭제할 필요가 없음
        managerRepository.save(updatedManager);
    }
}