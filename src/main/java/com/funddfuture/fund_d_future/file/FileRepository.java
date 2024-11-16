package com.funddfuture.fund_d_future.file;

import com.funddfuture.fund_d_future.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {

    @Query("SELECT f FROM File f WHERE f.id = :fileId")
    Optional<File> findById(@Param("fileId") UUID fileId);
}