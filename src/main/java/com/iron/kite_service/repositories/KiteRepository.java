package com.iron.kite_service.repositories;

import com.iron.kite_service.models.Kite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KiteRepository extends JpaRepository<Kite, Integer> {


    List<Kite> findKitesByOwner(String owner);

    List<Kite> findKitesByLocation(String location);


    List<Kite> findKitesByOwnerAndLocation(String ownerName, String location);
}
