package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
