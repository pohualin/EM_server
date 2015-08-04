package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Language;
import com.emmisolutions.emmimanager.model.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data Repo for externalized Strings
 */
public interface StringsRepository extends JpaRepository<Strings, Long> {

    /**
     * Find all Strings by language tag
     *
     * @param languageTag to lookup the Strings
     * @return a List of Strings
     */
    List<Strings> findByLanguageLanguageTag(String languageTag);

    Strings findByLanguageAndKey(Language language, String string);

    Page<Strings> findByKey(String key, Pageable pageable);
}
