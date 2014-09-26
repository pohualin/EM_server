package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamTagRepository extends JpaRepository<TeamTag, Long>, JpaSpecificationExecutor<TeamTag> {
    Page<TeamTag> findByTeam(Team team, Pageable pageable);
}
