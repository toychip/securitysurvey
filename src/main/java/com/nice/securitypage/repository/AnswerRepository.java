package com.nice.securitypage.repository;

import com.nice.securitypage.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    boolean existsByEmailname(String emailname);
}
