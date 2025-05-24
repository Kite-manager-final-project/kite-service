package com.iron.kite_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    @NonNull
    private String name;


    @NonNull
    private int phoneNumber;

    @NonNull
    private String email;
}
