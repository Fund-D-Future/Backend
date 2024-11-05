package com.funddfuture.fund_d_future.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface AuthRepository extends JpaRepository<AuthEntity, UUID> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM AuthEntity a WHERE a.email = ?1")
    Boolean existsByEmail(String email);
}