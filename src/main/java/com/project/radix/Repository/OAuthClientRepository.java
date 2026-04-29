package com.project.radix.Repository;

import com.project.radix.Model.OAuthClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthClientRepository extends JpaRepository<OAuthClient, Integer> {
    Optional<OAuthClient> findByClientId(String clientId);
    Optional<OAuthClient> findByClientIdAndIsActiveTrue(String clientId);
    boolean existsByClientId(String clientId);
}