package com.anubhav.dev.store.repositories;

import com.anubhav.dev.store.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}