package com.nice.securitypage.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDtoWrapper {
    private Map<String, AnswerDto> answerMap = new HashMap<>();
}
