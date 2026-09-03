package com.example.streamflix.repository;

import com.example.streamflix.entity.Profile;
import com.example.streamflix.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    List<Profile> findByUserOrderByIdAsc(User user);

    Optional<Profile> findFirstByUserOrderByIdAsc(User user);

    Optional<Profile> findByIdAndUser(Long id, User user);
}
