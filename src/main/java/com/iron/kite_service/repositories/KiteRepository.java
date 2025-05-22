package com.iron.kite_service.repositories;

import com.iron.kite_service.models.Kite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KiteRepository extends JpaRepository<Kite, Integer> {
}
