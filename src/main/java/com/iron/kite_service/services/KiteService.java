package com.iron.kite_service.services;

import com.iron.kite_service.clients.PersonFeignClient;
import com.iron.kite_service.dtos.KiteResponseDTO;
import com.iron.kite_service.dtos.KiteUpdatedLocationDTO;
import com.iron.kite_service.dtos.KiteUpdatedWindDTO;
import com.iron.kite_service.dtos.PersonDTO;
import com.iron.kite_service.exceptions.KiteNotFoundException;
import com.iron.kite_service.exceptions.OwnerNotFoundException;
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
        PersonDTO person = personFeignClient.getPersonByNickName(kite.getOwner());

        if (person == null)
            throw new OwnerNotFoundException("El dueño asignado a esta cometa no existe");

        return kiteRepository.save(kite);
    }

    //GET


    public KiteResponseDTO getKiteById(int id){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas buscar no existe");

        Kite kite = foundKite.get();

        PersonDTO person = personFeignClient.getPersonByNickName(kite.getOwner());

        return new KiteResponseDTO(kite, person);


    }



    public List<KiteResponseDTO> getAllKites(String username, String location){

        List<Kite> kites;

        if (username != null && location != null) //esto me da Internal server error
            kites =  kiteRepository.findKitesByOwnerAndLocation(username, location);
        else if (username != null) //esto me da Internal server error
            kites = kiteRepository.findKitesByOwner(username);
        else if (location != null) //esto me da Internal server error
            kites = kiteRepository.findKitesByLocation(location);
        else
            kites = kiteRepository.findAll();

        return kites.stream().map(kite -> {

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

    public Kite updateKite(int id, Kite kite){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas modificar no existe");

        PersonDTO person = personFeignClient.getPersonByNickName(kite.getOwner());

        if (person == null)
            throw new OwnerNotFoundException("El dueño asignado a esta cometa no existe");


        Kite kiteToChange = foundKite.get();

        final String OWNER = kiteToChange.getOwner();

        final String OWNER_KITE_RECEIVED = kite.getOwner();

        if (!OWNER.equals(OWNER_KITE_RECEIVED))
            throw new OwnerNotFoundException("No le puedes cambiar el dueño a una cometa, tienes que pasarle el mismo dueño");

        kiteToChange.setLocation(kite.getLocation());
        kiteToChange.setOwner(kite.getOwner());
        kiteToChange.setWindRequired(kite.getWindRequired());

        return kiteRepository.save(kiteToChange);

    }

    //PATCH

    public Kite updateWindRequiredKite(int id, KiteUpdatedWindDTO kite){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas modificar no existe");

        Kite kiteToUpdate = foundKite.get();

        kiteToUpdate.setWindRequired(kite.getWindRequired());

        return kiteRepository.save(kiteToUpdate);

    }

    public Kite updateLocationKite(int id, KiteUpdatedLocationDTO kite){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas modificar no existe");

        Kite kiteToUpdate = foundKite.get();

        kiteToUpdate.setLocation(kite.getLocation());

        return kiteRepository.save(kiteToUpdate);

    }


}
