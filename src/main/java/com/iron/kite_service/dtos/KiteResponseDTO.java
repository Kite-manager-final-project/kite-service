package com.iron.kite_service.dtos;

import com.iron.kite_service.models.Kite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KiteResponseDTO {

    private int id;

    private int windRequired;

    private String location;

    private PersonDTO owner;

    public KiteResponseDTO(Kite kite, PersonDTO owner){
        setId(kite.getId());
        setWindRequired(kite.getWindRequired());
        setLocation(kite.getLocation());
        setOwner(owner);
    }
}
