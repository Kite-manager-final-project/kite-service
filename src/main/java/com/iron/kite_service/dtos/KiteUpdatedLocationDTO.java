package com.iron.kite_service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KiteUpdatedLocationDTO {

    @NotBlank(message = "La ubicación no puede estar vacia")
    private String location;
}
