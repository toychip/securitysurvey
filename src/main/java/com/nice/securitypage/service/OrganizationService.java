package com.nice.securitypage.service;

import com.nice.securitypage.entity.Organization;
import com.nice.securitypage.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public List<Organization> getOrganization() {
        return organizationRepository.findAll();
    }
}
