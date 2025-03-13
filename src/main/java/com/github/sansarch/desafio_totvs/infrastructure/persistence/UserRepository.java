package com.github.sansarch.desafio_totvs.infrastructure.persistence;

import com.github.sansarch.desafio_totvs.infrastructure.persistence.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    UserDetails findByUsername(String username);
}
