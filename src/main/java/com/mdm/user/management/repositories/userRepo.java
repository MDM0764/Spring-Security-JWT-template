package com.mdm.user.management.repositories;

import com.mdm.user.management.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepo extends JpaRepository<AppUser, Integer> {

    Optional<AppUser> findByEmail(String email);
}
