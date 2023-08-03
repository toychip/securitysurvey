package com.nice.securitypage.service;

import com.nice.securitypage.entity.Organization;
import com.nice.securitypage.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    // Organization 테이블의 모든 레코드를 조회하고, 이들의 들여쓰기 레벨을 계산하여 맵으로 반환하는 메소드
    public Map<String, Integer> getOrganization() {
        // 모든 Organization 조회
        List<Organization> organizations = organizationRepository.findAll();
        // 조회한 결과를 저장할 LinkedHashMap을 생성
        Map<String, Integer> organizationIndentation = new LinkedHashMap<>();
        // 조회한 Organization 객체 필드가 계층이 어떤지 구하는 메서드
        organize(organizations, organizationIndentation, 0, 0);
        return organizationIndentation;

        /* return ->
                "대표이사" : 0,
                "리서치사업본부" : 1, "동반성장사업본부" : 1,
                "디지털혁신본부" : 1, "기획관리본부" : 1, "DT실" : 1,
                "FMR실" : 2, "BI실" : 2, "RI실" : 2, "DI실" : 2, "동반성장실" : 2,
                "플랫폼네트워크사업실" : 2, "IT혁신실" : 2, "스마트데이터Biz실" : 2, "경영지원실" : 2
                 */

    }

    // 주어진 Organization 객체들을 재귀적으로 정렬하고 들여쓰기 레벨을 계산하는 메소드
    private void organize(List<Organization> organizations, Map<String, Integer> map, int parentId, int level) {
        for (Organization org : organizations) {
            //  현재 Organization 객체의 부모 ID가 주어진 부모 ID와 같다면
            if (org.getPId() == parentId) {
                // 현재 Organization 객체의 이름과 그 들여쓰기 레벨을 맵에 추가
                map.put(org.getName(), level);
                // 현재 Organization 객체의 ID를 부모 ID로, 들여쓰기 레벨을 1 증가시켜서 재귀 호출
                organize(organizations, map, org.getId(), level + 1);

            }
        }
    }
}
