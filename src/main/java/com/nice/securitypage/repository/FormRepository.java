package com.nice.securitypage.repository;

import com.nice.securitypage.entity.Form;
import org.springframework.data.repository.CrudRepository;

public interface FormRepository extends CrudRepository<Form, Long> {
    boolean existsByEmailname(String emailname);
}
