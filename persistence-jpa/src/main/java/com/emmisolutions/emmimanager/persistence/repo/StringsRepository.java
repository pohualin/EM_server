package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Strings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data Repo for externalized Strings
 */
public interface StringsRepository extends JpaRepository<Strings, Long>{

    List<Strings> findByLanguageLanguageTag(String languageTag);
}
