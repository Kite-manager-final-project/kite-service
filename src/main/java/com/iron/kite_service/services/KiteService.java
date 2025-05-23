package com.iron.kite_service.services;

import com.iron.kite_service.exceptions.KiteNotFoundException;
import com.iron.kite_service.models.Kite;
import com.iron.kite_service.repositories.KiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class KiteService {

    //todo: implementar put y patch

    @Autowired
    private KiteRepository kiteRepository;

    //POST

    public Kite saveKite(Kite kite){
        return kiteRepository.save(kite);
    }

    //GET


    public ResponseEntity<?> getKiteById(int id){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isPresent())
            return new ResponseEntity<>(foundKite.get(), HttpStatus.OK);
        else
            throw new KiteNotFoundException("La cometa que intentas buscar no existe");

    }

    public List<Kite> getAllKites(String username, String location){

        if (username != null && location != null)
            return kiteRepository.findKitesByOwnerAndLocation(username, location);

        if (username != null)
            return kiteRepository.findKitesByOwner(username);

        if (location != null)
            return kiteRepository.findKitesByLocation(location);


        return kiteRepository.findAll();
    }

    //DELETE

    public void deleteKite(int id){

        var kiteToDelete = kiteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        kiteRepository.delete(kiteToDelete);
    }

    //PUT

    public ResponseEntity<?> updateKite(int id, Kite kite){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isPresent()){
            Kite kiteToChange = foundKite.get();

            kiteToChange.setLocation(kite.getLocation());
            kiteToChange.setOwner(kite.getOwner());
            kiteToChange.setWindRequired(kite.getWindRequired());

            Kite savedKite = kiteRepository.save(kiteToChange);

            return new ResponseEntity<>(savedKite, HttpStatus.OK);
        }
        else
            throw new KiteNotFoundException("La cometa que intentas modificar no existe");
    }


    //PATCH


}
