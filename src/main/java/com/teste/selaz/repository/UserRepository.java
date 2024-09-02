package com.teste.selaz.repository;

import com.teste.selaz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long>{
    UserDetails findByUsername(String username);

    @Query("SELECT u.id FROM users u WHERE u.username = :username")
    Long findIdByUsername(@Param("username") String username);


}
