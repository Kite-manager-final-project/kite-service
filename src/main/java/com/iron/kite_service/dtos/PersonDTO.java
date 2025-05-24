package com.iron.kite_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {


    private String name;


    private int phoneNumber;


    private String email;
}
