package com.nice.securitypage.service;

import com.nice.securitypage.DateConfig;
import com.nice.securitypage.dto.FormDto;
import com.nice.securitypage.entity.Form;
import com.nice.securitypage.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    public void write(FormDto formDto, String clientIP, String clientBrowser) {

        String phoneNumber = formDto.getPhoneNumber1() + formDto.getPhoneNumber2() + formDto.getPhoneNumber3();
        String extensionNumber = formDto.getExtensionNumber1() + formDto.getExtensionNumber2() + formDto.getExtensionNumber3();
        String emailname = formDto.getEmailname() + "@nicednr.co.kr";
        Form form = Form.builder()
                .name(formDto.getName())
                .emailname(emailname)
                .phoneNumber(phoneNumber)
                .extensionNumber(extensionNumber)
                .ip(clientIP)
                .browser(clientBrowser)
                .build();

        formRepository.save(form);
    }

    // ip와 browser 정보를 넣은 빈 껍데기 만들기
    public FormDto getFormDto(String clientIP, String clientBrowser) {
        FormDto formDto = FormDto.builder()
                .ip(clientIP)
                .browser(clientBrowser)
                .build();
        return formDto;
    }

    public FormDto updatedFormDto(FormDto inputFormDto, String clientIP, String clientBrowser) {
        FormDto formDto = FormDto.builder()
                .name(inputFormDto.getName())
                .emailname(inputFormDto.getEmailname())
                .phoneNumber1(inputFormDto.getPhoneNumber1())
                .phoneNumber2(inputFormDto.getPhoneNumber2())
                .phoneNumber3(inputFormDto.getPhoneNumber3())
                .extensionNumber1(inputFormDto.getExtensionNumber1())
                .extensionNumber2(inputFormDto.getExtensionNumber2())
                .extensionNumber3(inputFormDto.getExtensionNumber3())
                .ip(clientIP)
                .browser(clientBrowser)
                .build();
        return formDto;
    }

    public String isOutOfDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate fromDate = LocalDate.parse(DateConfig.FROM_DATE, formatter);
        LocalDate toDate = LocalDate.parse(DateConfig.TO_DATE, formatter);

        if (LocalDate.now().isBefore(fromDate) || LocalDate.now().isAfter(toDate)) {
            return "outOfDate"; // 날짜가 하나라도 이상할 경우 기간이 아님을 뜻하는 outOfDate로 Return
        }
        return null;
    }
}
