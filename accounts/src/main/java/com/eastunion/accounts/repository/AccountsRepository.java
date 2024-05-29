package com.eastunion.accounts.repository;

import com.eastunion.accounts.entity.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    Optional<Accounts> findByCustomerId(Long customerId);

    //using these annotations because my custom method is modifying the database
    @Transactional
    @Modifying
    void deleteByCustomerId(Long customerId);
}
