package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data Repo for Language Support
 */
public interface LanguageRepository extends JpaRepository<Language, Long> {

    /**
     * finds a language by given tag
     *
     * @param tag
     * @return a language
     */
    Language findByLanguageTag(String tag);

    /**
     * finds all available languages
     *
     * @param page given page size
     * @return a page of languages
     */
    Page<Language> findAll(Pageable page);
}
