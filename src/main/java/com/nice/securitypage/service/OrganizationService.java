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
    public Map<Organization, Integer> getOrganization() {
        // 모든 Organization 조회
        List<Organization> organizations = organizationRepository.findAll();
        // 조회한 결과를 저장할 LinkedHashMap을 생성
        Map<Organization, Integer> organizationIndentation = new LinkedHashMap<>();
        // 조회한 Organization 객체들을 재귀적으로 정렬하고 들여쓰기 레벨을 계산하는 메소드 호출
        organize(organizations, organizationIndentation, 0, 0);
        return organizationIndentation;
    }

    // 주어진 Organization 객체들을 재귀적으로 정렬하고 들여쓰기 레벨을 계산하는 메소드
    private void organize(List<Organization> organizations, Map<Organization, Integer> map, int parentId, int level) {
        for (Organization org : organizations) {
            //  현재 Organization 객체의 부모 ID가 주어진 부모 ID와 같다면
            if (org.getPId() == parentId) {
                // 현재 Organization 객체와 그 들여쓰기 레벨을 맵에 추가
                map.put(org, level);
                // 현재 Organization 객체의 ID를 부모 ID로, 들여쓰기 레벨을 1 증가시켜서 재귀 호출
                organize(organizations, map, org.getId(), level + 1);
            }
        }
    }
}
