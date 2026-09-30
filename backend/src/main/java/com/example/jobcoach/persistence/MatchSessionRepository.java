package com.example.jobcoach.persistence;

import com.example.jobcoach.domain.MatchSession;

import java.util.Optional;

/** Persistence port; the MySQL adapter will be added after the schema is confirmed. */
public interface MatchSessionRepository {
    MatchSession save(MatchSession session);

    Optional<MatchSession> findById(String id);
}
