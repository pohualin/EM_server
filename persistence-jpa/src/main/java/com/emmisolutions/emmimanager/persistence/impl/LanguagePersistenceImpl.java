package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Language;
import com.emmisolutions.emmimanager.persistence.LanguagePersistence;
import com.emmisolutions.emmimanager.persistence.repo.LanguageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Persistence implementation for LanguagePersistence
 */
@Repository
public class LanguagePersistenceImpl implements LanguagePersistence {

    @Resource
    LanguageRepository languageRepository;

    @Override
    public Page<Language> list(Pageable page) {
        return languageRepository.findAll(page == null ? new PageRequest(0, 50, Sort.Direction.ASC, "id") : page);
    }
}
