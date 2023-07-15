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

        String phoneNumber = formDto.getPhoneNumber1() + formDto.getPhoneNumber2() + formDto.getPhoneNumber3();
        String extensionNumber = formDto.getExtensionNumber1() + formDto.getExtensionNumber2() + formDto.getExtensionNumber3();

        Form form = Form.builder()
                .name(formDto.getName())
                .emailname(formDto.getEmailname())
                .phoneNumber(phoneNumber)
                .extensionNumber(extensionNumber)
                .ip(formDto.getIp())
                .browser(formDto.getBrowser())
                .build();

        formRepository.save(form);
    }
}
