package com.finance.repository;


import com.finance.entity.RefreshToken;
import com.finance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

        Optional<RefreshToken> findByToken(String token);

        //void deleteByUser(User user);
    void deleteByUser_Id(Long userId);
}
