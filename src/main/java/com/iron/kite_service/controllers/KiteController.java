package com.iron.kite_service.controllers;

import com.iron.kite_service.models.Kite;
import com.iron.kite_service.services.KiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kite")
public class KiteController {

    @Autowired
    private KiteService kiteService;

    //GET

    @GetMapping("/allKites")
    public ResponseEntity<List<Kite>> getAllKites(@RequestParam(required = false) String owner,
                                                  @RequestParam(required = false) String location){

        return ResponseEntity.ok(kiteService.getAllKites(owner, location));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKiteById(@PathVariable int id){
        return ResponseEntity.ok(kiteService.getKiteById(id));
    }

    //POST


    //PUT


    //PATCH


    //DELETE


}
