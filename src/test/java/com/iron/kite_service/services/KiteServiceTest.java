package com.iron.kite_service.services;

import com.iron.kite_service.models.Kite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KiteServiceTest {

    @Autowired
    KiteService kiteService;


    @Test
    @DisplayName("Asigno 3 cometas al hombre de la rae, que las va a usar en distintas ubicaciones")
    void insert3Kites(){

        Kite kite1, kite2, kite3;

        kite1 = new Kite(23, "Madrid", "hombre_de_la_rae");

        kite2 = new Kite(23, "Caraquiz, Uceda (Guadalajara)", "hombre_de_la_rae");

        kite3 = new Kite(23, "Torrevieja (Alicante)", "hombre_de_la_rae");

        Kite savedKite1, savedKite2, savedKite3;

        savedKite1 = kiteService.saveKite(kite1);
        savedKite2 = kiteService.saveKite(kite2);
        savedKite3 = kiteService.saveKite(kite3);

        System.out.println("================================");
        System.out.println("Estas son las cometas que has guardado");
        System.out.println(savedKite1);
        System.out.println(savedKite2);
        System.out.println(savedKite3);
        System.out.println("================================");

    }

    @Test
    @DisplayName("Asigno una cometa a auronplay que la utilizará en Madrid")
    void assignKiteToAuronplayThatUseInMadrid(){
        Kite kite = new Kite(23, "Madrid", "auronplay");

        Kite saveKite = kiteService.saveKite(kite);

        System.out.println("================================");
        System.out.println("Esta es la cometa que has guardado");
        System.out.println(saveKite);
        System.out.println("================================");
    }

    /*@Test
    @DisplayName("Busco las cometas que tiene hombre_de_la_rae en Madrid")
    void findKiteByOwnerAndLocation(){
        List<Kite> foundKites = kiteService.getAllKites("hombre_de_la_rae", "Madrid");

        System.out.println("================================");
        System.out.println("Estas son las cometas que hemos encontrado");
        System.out.println(foundKites);
        System.out.println("================================");
    }

    @Test
    @DisplayName("Busco las cometas que tiene hombre_de_la_rae")
    void finsKitesHombreDeLaRae(){
        List<Kite> foundKites = kiteService.getAllKites("hombre_de_la_rae", null);

        System.out.println("================================");
        System.out.println("Estas son las cometas que hemos encontrado");
        System.out.println(foundKites);
        System.out.println("================================");
    }

    @Test
    @DisplayName("Busco las cometas que están registradas en Madrid")
    void finsKitesInMadrid(){
        List<Kite> foundKites = kiteService.getAllKites(null, "Madrid");

        System.out.println("================================");
        System.out.println("Estas son las cometas que hemos encontrado");
        System.out.println(foundKites);
        System.out.println("================================");
    }*/


}