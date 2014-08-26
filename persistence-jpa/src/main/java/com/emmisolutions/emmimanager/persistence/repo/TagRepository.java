package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag>{
}
