package com.nice.securitypage.service;

import com.nice.securitypage.config.security.EncryptionUtil;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService implements UserDetailsService {

    private final ManagerRepository managerRepository;
    private final AnswerRepository answerRepository;

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

    public List<Answer> getAnswers() {
        return answerRepository.findAll();
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
