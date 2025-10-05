package com.reyesemf.gm.article.datasource.repository;

import com.reyesemf.gm.article.domain.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByToken(String token);
}
