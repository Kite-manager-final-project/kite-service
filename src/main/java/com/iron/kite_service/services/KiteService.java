package com.iron.kite_service.services;

import com.iron.kite_service.clients.PersonFeignClient;
import com.iron.kite_service.dtos.KiteResponseDTO;
import com.iron.kite_service.dtos.KiteUpdatedLocationDTO;
import com.iron.kite_service.dtos.KiteUpdatedWindDTO;
import com.iron.kite_service.dtos.PersonDTO;
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
import java.util.stream.Collectors;

@Service
public class KiteService {

    @Autowired
    private KiteRepository kiteRepository;

    @Autowired
    private PersonFeignClient personFeignClient;

    //POST

    public Kite saveKite(Kite kite){
        return kiteRepository.save(kite);
    }

    //GET


    public ResponseEntity<?> getKiteById(int id){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas buscar no existe");

        Kite kite = foundKite.get();

        PersonDTO person = personFeignClient.getPersonByNickName(kite.getOwner());

        KiteResponseDTO response = new KiteResponseDTO(kite, person);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public List<KiteResponseDTO> getAllKites(String username, String location){

        if (username != null && location != null)
            return kiteRepository.findKitesByOwnerAndLocation(username, location);

        if (username != null)
            return kiteRepository.findKitesByOwner(username);

        if (location != null)
            return kiteRepository.findKitesByLocation(location);

        List<Kite> allKites = kiteRepository.findAll();

        return allKites.stream().map(kite -> {

            PersonDTO person = personFeignClient.getPersonByNickName(kite.getOwner());

            return new KiteResponseDTO(kite, person);
        }).collect(Collectors.toList());


    }

    //DELETE

    public void deleteKite(int id){

        Optional<Kite> kiteToDelete = kiteRepository.findById(id);

        if (kiteToDelete.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas eliminar no existe");

        kiteRepository.delete(kiteToDelete.get());

    }

    //PUT

    public ResponseEntity<?> updateKite(int id, Kite kite){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas modificar no existe");

        Kite kiteToChange = foundKite.get();

        kiteToChange.setLocation(kite.getLocation());
        kiteToChange.setOwner(kite.getOwner());
        kiteToChange.setWindRequired(kite.getWindRequired());

        Kite updatedKite = kiteRepository.save(kiteToChange);

        return new ResponseEntity<>(updatedKite, HttpStatus.OK);

    }

    //PATCH

    public ResponseEntity<?> updateWindRequiredKite(int id, KiteUpdatedWindDTO kite){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas modificar no existe");

        Kite kiteToUpdate = foundKite.get();

        kiteToUpdate.setWindRequired(kite.getWindRequired());

        Kite updatedKite = kiteRepository.save(kiteToUpdate);

        return new ResponseEntity<>(updatedKite, HttpStatus.OK);

    }

    public ResponseEntity<?> updateLocationKite(int id, KiteUpdatedLocationDTO kite){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas modificar no existe");

        Kite kiteToUpdate = foundKite.get();

        kiteToUpdate.setLocation(kite.getLocation());

        Kite updatedKite = kiteRepository.save(kiteToUpdate);

        return new ResponseEntity<>(updatedKite, HttpStatus.OK);

    }


}
