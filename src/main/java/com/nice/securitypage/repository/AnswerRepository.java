package com.nice.securitypage.repository;

import com.nice.securitypage.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // Answer 테이블에 Emailname 컬럼중 파라미터 (emailname) 값이 존재하는가?
    boolean existsByEmailname(String emailname);

    /*
    Mysql
    SELECT EXISTS(SELECT 1 Answer answer WHERE emailname = ?)
     */
}
