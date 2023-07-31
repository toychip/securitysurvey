package com.nice.securitypage.service;

import com.nice.securitypage.config.AESUtilConfig;
import com.nice.securitypage.config.security.EncryptionUtil;
import com.nice.securitypage.dto.AnswerResponse;
import com.nice.securitypage.dto.ManagerDto;
import com.nice.securitypage.entity.Answer;
import com.nice.securitypage.entity.Manager;
import com.nice.securitypage.repository.AnswerRepository;
import com.nice.securitypage.repository.ManagerRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService implements UserDetailsService {

    private final ManagerRepository managerRepository;
    private final AnswerRepository answerRepository;
    private final AESUtilConfig aesUtilConfig;

//    public boolean login(ManagerDto managerDto, HttpSession session) {
//        String username = managerDto.getUsername();
//        String providedPassword = managerDto.getPassword();
//
//        // Encrypt the provided password
//        String md5Password = EncryptionUtil.encryptMD5(providedPassword);
//        String sha256Password = EncryptionUtil.encryptSHA256(md5Password);
//
//        Manager manager = managerRepository.findByUsername(username)
//                .orElse(null);
//
//        if (manager == null) {
//            return false;
//        }
//
//        // Check the provided password against the one in the database
//        if (!manager.getPassword().equals(sha256Password)) {
//            Integer failureCount = (Integer) session.getAttribute(username + "-failureCount");
//            if (failureCount == null) {
//                failureCount = 1;
//            } else {
//                failureCount++;
//            }
//
//            session.setAttribute(username + "-failureCount", failureCount);
//
//            // If the failure count is 5, lock the user
//            if (failureCount >= 5) {
//                lockUser(manager);
//            }
//
//            return false;
//        }
//
//        // If everything is fine, return true
//        session.removeAttribute(username + "-failureCount");
//        return true;
//    }
    private void lockUser(Manager manager) {
        manager.lock();
        managerRepository.save(manager);
    }

//    public boolean isLocked(String username) {
//        Manager manager = managerRepository.findByUsername(username)
//                .orElse(null);
//
//        if (manager == null) {
//            return false;
//        }
//
//        return manager.isLocked();
//    }

    public List<AnswerResponse> getAnswers() {
        List<Answer> answers = answerRepository.findAll();
        List<AnswerResponse> answerResponses = new ArrayList<>();

        for (Answer answer : answers) {
            AnswerResponse.AnswerResponseBuilder answerResponseBuilder = AnswerResponse.builder()
                    .id(answer.getId())
                    .content(answer.getContent())
                    .isRequired(answer.getIsRequired())
                    .emailName(answer.getEmailname())
                    .response(answer.getResponse());

            String url = "https://nicednr-project.s3.ap-northeast-2.amazonaws.com/nice-imformation-security/";
            if (answer.getResponse().startsWith(url)) {
                String encryptedPart = answer.getResponse().replace(url, "");
                String decryptedPart = aesUtilConfig.decrypt(encryptedPart);
                answerResponseBuilder.decryptedResponse(decryptedPart);
            }

            answerResponses.add(answerResponseBuilder.build());
        }

        return answerResponses;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = managerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(manager.getUsername())
                .password(manager.getPassword())
                .build();
    }

}
