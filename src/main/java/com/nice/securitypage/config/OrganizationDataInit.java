package com.nice.securitypage.config;

import com.nice.securitypage.entity.Organization;
import com.nice.securitypage.repository.OrganizationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

//@Component
@RequiredArgsConstructor
// 스프링 컨테이너 실행시 데이터 등록
public class OrganizationDataInit {
    private final OrganizationRepository organizationRepository;

//    @PostConstruct
    public void init() {
        Organization org1 = Organization.builder()
                .name("대표이사")
                .pId(0)
                .build();

        Organization org2 = Organization.builder()
                .name("리서치사업본부")
                .pId(1)
                .build();

        Organization org3 = Organization.builder()
                .name("동반성장사업본부")
                .pId(1)
                .build();

        Organization org4 = Organization.builder()
                .name("디지털혁신본부")
                .pId(1)
                .build();

        Organization org5 = Organization.builder()
                .name("기획관리본부")
                .pId(1)
                .build();

        Organization org6 = Organization.builder()
                .name("DT실")
                .pId(1)
                .build();

        Organization org7 = Organization.builder()
                .name("FMR실")
                .pId(2)
                .build();

        Organization org8 = Organization.builder()
                .name("BI실")
                .pId(2)
                .build();

        Organization org9 = Organization.builder()
                .name("RI실")
                .pId(2)
                .build();

        Organization org10 = Organization.builder()
                .name("DI실")
                .pId(2)
                .build();

        Organization org11 = Organization.builder()
                .name("동반성장실")
                .pId(3)
                .build();

        Organization org12 = Organization.builder()
                .name("플랫폼네트워크사업실")
                .pId(3)
                .build();

        Organization org13 = Organization.builder()
                .name("IT혁신실")
                .pId(4)
                .build();

        Organization org14 = Organization.builder()
                .name("스마트데이터Biz실")
                .pId(5)
                .build();

        Organization org15 = Organization.builder()
                .name("경영지원실")
                .pId(5)
                .build();

        List<Organization> organizations = Arrays.asList
                (org1, org2, org3, org4, org5, org6, org7, org8,
                org9, org10, org11, org12, org13, org14, org15);

        organizationRepository.saveAll(organizations);
    }
}
