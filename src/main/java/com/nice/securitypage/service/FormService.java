package com.nice.securitypage.service;

import com.nice.securitypage.dto.FormDto;
import com.nice.securitypage.entity.Form;
import com.nice.securitypage.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    public void write(FormDto formDto) {
        Form form = Form.builder()
                .name(formDto.getName())
                .emailname(formDto.getEmailname())
                .phoneNumber(formDto.getPhoneNumber())
                .extensionNumber(formDto.getExtensionNumber())
                .ip(formDto.getIp())
                .browser(formDto.getBrowser())
                .build();

        formRepository.save(form);
    }

}
