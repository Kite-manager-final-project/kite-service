package com.iron.kite_service.services;

import com.iron.kite_service.clients.PersonFeignClient;
import com.iron.kite_service.dtos.KiteResponseDTO;
import com.iron.kite_service.dtos.KiteUpdatedLocationDTO;
import com.iron.kite_service.dtos.KiteUpdatedWindDTO;
import com.iron.kite_service.dtos.PersonDTO;
import com.iron.kite_service.exceptions.KiteNotFoundException;
import com.iron.kite_service.exceptions.OwnerNotFoundException;
import com.iron.kite_service.exceptions.OwnerPreviusAssignException;
import com.iron.kite_service.models.Kite;
import com.iron.kite_service.repositories.KiteRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Guarda una cometa en la base de datos
     * @param kite la cometa a guardar en la base de datos
     * @return la cometa que le ha mandado el cliente
     * @throws FeignException.NotFound lanza esta excepción si el dueño a asignar no existe
     */
    public Kite saveKite(Kite kite){
        try {
            personFeignClient.getPersonByNickName(kite.getOwner());
            return kiteRepository.save(kite);
        }catch (FeignException.NotFound e) {
            throw new OwnerNotFoundException("El dueño '" + kite.getOwner() + "' no existe.");
        }

    }

    //GET

    /**
     * Busca una cometa por id
     * @param id
     * @return Devuelve esa cometa con el id especificado
     */
    public KiteResponseDTO getKiteById(int id){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas buscar no existe");

        Kite kite = foundKite.get();

        PersonDTO person = personFeignClient.getPersonByNickName(kite.getOwner()); //aqui falla

        return new KiteResponseDTO(kite, person);


    }

    /**
     * Busca todas las cometas, los parámetros son opcionales, si no quieres filtrar ciertos parámetros, hay que mandarlos como null
     * @param username para filtrar las cometas por owner
     * @param location para filtrar las cometas por location
     * @return devuelve varias cometas que cumplan los filtros especificados, o todas si no hay especificación
     */
    public List<KiteResponseDTO> getAllKites(String username, String location){

        List<Kite> kites;

        if (username != null && location != null)
            kites =  kiteRepository.findKitesByOwnerAndLocation(username, location);
        else if (username != null)
            kites = kiteRepository.findKitesByOwner(username);
        else if (location != null)
            kites = kiteRepository.findKitesByLocation(location);
        else
            kites = kiteRepository.findAll(); //cuando no le paso ningun parametro, me da error, y antes me funcionaba, no he tocado nada

        return kites.stream().map(kite -> {

            PersonDTO person = personFeignClient.getPersonByNickName(kite.getOwner());

            return new KiteResponseDTO(kite, person);
        }).collect(Collectors.toList());


    }

    //DELETE

    /**
     * Para eliminar una cometa
     * @param id El id de la cometa a eliminar
     * @throws KiteNotFoundException si no existe ninguna cometa con ese ID
     */
    public void deleteKite(int id){

        Optional<Kite> kiteToDelete = kiteRepository.findById(id);

        if (kiteToDelete.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas eliminar no existe");

        kiteRepository.delete(kiteToDelete.get());

    }

    //PUT

    /**
     * Modifica todos los campos de una cometa, salvo el dueño. Aún así, hay que especificar el dueño
     * @param id El id de la cometa a modificar
     * @param kite La cometa en sí con los nuevos datos
     * @return la cometa modificada
     */
    public Kite updateKite(int id, Kite kite){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas modificar no existe");




        Kite kiteToChange = foundKite.get();

        final String OWNER = kiteToChange.getOwner();

        final String OWNER_KITE_RECEIVED = kite.getOwner();

        if (!OWNER.equals(OWNER_KITE_RECEIVED))
            throw new OwnerPreviusAssignException("No le puedes cambiar el dueño a una cometa, tienes que pasarle el mismo dueño");

        kiteToChange.setLocation(kite.getLocation());
        kiteToChange.setOwner(kite.getOwner());
        kiteToChange.setWindRequired(kite.getWindRequired());

        return kiteRepository.save(kiteToChange);

    }

    //PATCH

    /**
     * Permite modificar el viento requerido de la cometa
     * @param id El id de la cometa a modificar
     * @param kite Una cometa DTO con el viento requerido especificado
     * @return Esa cometa con el viento requerido modificado
     * @throws KiteNotFoundException Si no existe ninguna cometa con ese ID
     */
    public Kite updateWindRequiredKite(int id, KiteUpdatedWindDTO kite){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas modificar no existe");

        Kite kiteToUpdate = foundKite.get();

        kiteToUpdate.setWindRequired(kite.getWindRequired());

        return kiteRepository.save(kiteToUpdate);

    }

    /**
     * Permite modificar la ubicación de donde se va a utilizar esa cometa
     * @param id El id de la cometa a modificar
     * @param kite Una cometa DTO con la ubicación especificada
     * @return Esa cometa con la ubicación modificada
     * @throws KiteNotFoundException Si no existe ninguna cometa con ese ID
     */
    public Kite updateLocationKite(int id, KiteUpdatedLocationDTO kite){
        Optional<Kite> foundKite = kiteRepository.findById(id);

        if (foundKite.isEmpty())
            throw new KiteNotFoundException("La cometa que intentas modificar no existe");

        Kite kiteToUpdate = foundKite.get();

        kiteToUpdate.setLocation(kite.getLocation());

        return kiteRepository.save(kiteToUpdate);

    }

}
