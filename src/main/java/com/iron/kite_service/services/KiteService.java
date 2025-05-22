package com.iron.kite_service.services;

import com.iron.kite_service.models.Kite;
import com.iron.kite_service.repositories.KiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class KiteService {

    @Autowired
    private KiteRepository kiteRepository;

    public Kite saveKite(Kite kite){
        return kiteRepository.save(kite);
    }
}
