package com.nice.securitypage.service;

import com.nice.securitypage.config.DateConfig;
import com.nice.securitypage.dto.FormDto;
import com.nice.securitypage.entity.Form;
import com.nice.securitypage.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    public void write(FormDto formDto, String clientIP, String clientBrowser) {

        // 전화번호 합치기
        String phoneNumber = formDto.getPhoneNumber1() + formDto.getPhoneNumber2() + formDto.getPhoneNumber3();
        // 사내번호 합치기
        String extensionNumber = formDto.getExtensionNumber1() + formDto.getExtensionNumber2() + formDto.getExtensionNumber3();
        // 입력한 필드에 사내메일주소 합치기
        String emailname = formDto.getEmailname() + "@nicednr.co.kr";

        // builder 형식으로 객체 생성
        Form form = Form.builder()
                .name(formDto.getName())
                .emailname(emailname)
                .phoneNumber(phoneNumber)
                .extensionNumber(extensionNumber)
                .ip(clientIP)
                .browser(clientBrowser)
                .build();

        // 데이터 저장
        formRepository.save(form);
    }

    // 처음 렌더링용 ip와 browser 정보를 넣은 빈 껍데기 만들기
    public FormDto getFormDto(String clientIP, String clientBrowser) {
        FormDto formDto = FormDto.builder()
                .ip(clientIP)
                .browser(clientBrowser)
                .build();
        return formDto;
    }

    // 필드 오류가 났을 때 사용자가 검증에 통과한 필드만 저장 후 ip와 브라우저 정보를 넣은 폼으로 재렌더링
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

    // 날짜 포멧터를 활용하여 DataConfig에서 시작 년월일, 만료 년월일 가져와 Model에 추가
    public void getPeriod(Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate fromDate = LocalDate.parse(DateConfig.FROM_DATE, formatter);
        LocalDate toDate = LocalDate.parse(DateConfig.TO_DATE, formatter);

        model.addAttribute("fromYear", fromDate.getYear());
        model.addAttribute("fromMonth", fromDate.getMonthValue());
        model.addAttribute("fromDay", fromDate.getDayOfMonth());

        model.addAttribute("toYear", toDate.getYear());
        model.addAttribute("toMonth", toDate.getMonthValue());
        model.addAttribute("toDay", toDate.getDayOfMonth());
    }

    // 날짜 검증 로직
    public String isOutOfDate() {

        // 포멧터를 이용, DataConfig로 날짜 가져옴
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate fromDate = LocalDate.parse(DateConfig.FROM_DATE, formatter);
        LocalDate toDate = LocalDate.parse(DateConfig.TO_DATE, formatter);

        // 날짜가 하나라도 이상할 경우 기간이 아님을 뜻하는 outOfDate로 Return
        if (LocalDate.now().isBefore(fromDate) || LocalDate.now().isAfter(toDate)) {
            return "outOfDate";
        }
        return null;
    }

    public boolean isAlready(String emailname) {


        return formRepository.existsByEmailname(emailname);
    }



}
