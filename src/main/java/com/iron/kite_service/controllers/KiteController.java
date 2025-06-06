package com.iron.kite_service.controllers;

import com.iron.kite_service.dtos.KiteResponseDTO;
import com.iron.kite_service.dtos.KiteUpdatedLocationDTO;
import com.iron.kite_service.dtos.KiteUpdatedWindDTO;
import com.iron.kite_service.exceptions.KiteNotFoundException;
import com.iron.kite_service.exceptions.OwnerNotFoundException;
import com.iron.kite_service.exceptions.OwnerPreviusAssignException;
import com.iron.kite_service.models.Kite;
import com.iron.kite_service.services.KiteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kite")
public class KiteController {

    @Autowired
    private KiteService kiteService;

    //GET

    @GetMapping("/allKites")
    public ResponseEntity<List<KiteResponseDTO>> getAllKites(@RequestParam(required = false) String owner,
                                                             @RequestParam(required = false) String location){

        return ResponseEntity.ok(kiteService.getAllKites(owner, location));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKiteById(@PathVariable int id){
        try {
            return ResponseEntity.ok(kiteService.getKiteById(id));
        }catch (KiteNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage(), "status", 404));
        }

    }

    //POST

    @PostMapping
    public ResponseEntity<?> saveKite(@Valid @RequestBody Kite kite){
        try {
            System.out.println("Cometa recibida: "+kite);
            return ResponseEntity.ok(kiteService.saveKite(kite));
        }catch (OwnerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage(), "status", 404));
        }

    }


    //PUT

    @PutMapping("/{id}")
    public ResponseEntity<?> updateKite(@PathVariable int id, @Valid @RequestBody Kite kite){
        try {
            return ResponseEntity.ok(kiteService.updateKite(id, kite));
        }catch (KiteNotFoundException | OwnerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage(), "status", 404));
        } catch (OwnerPreviusAssignException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage(), "status", 409));
        }
    }


    //PATCH

    @PatchMapping("/updateWindRequired/{id}")
    public ResponseEntity<?> updateWindRequired(@PathVariable int id, @Valid @RequestBody KiteUpdatedWindDTO kite){
        try {
            return ResponseEntity.ok(kiteService.updateWindRequiredKite(id, kite));
        }catch (KiteNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage(), "status", 404));
        }
    }

    @PatchMapping("/updateLocation/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable int id, @Valid @RequestBody KiteUpdatedLocationDTO kite){
        try {
            return ResponseEntity.ok(kiteService.updateLocationKite(id, kite));
        }catch (KiteNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage(), "status", 404));
        }
    }


    //DELETE

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKite(@PathVariable int id){
        try {
            kiteService.deleteKite(id);
            return ResponseEntity.ok("Esa cometa ha sido eliminada correctamente");
        }catch (KiteNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage(), "status", 404));
        }
    }
}
