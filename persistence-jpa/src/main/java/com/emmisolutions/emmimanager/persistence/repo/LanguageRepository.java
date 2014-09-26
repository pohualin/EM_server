package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data Repo for Language Support
 */
public interface LanguageRepository extends JpaRepository<Language, Long>{

}
