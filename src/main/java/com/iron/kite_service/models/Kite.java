package com.iron.kite_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "kites")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Kite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @NotNull(message = "El viento requerido no puede estar vacio")
    @Min(value = 14, message = "El viento mínimo requerido es 14")
    @Max(value = 40, message = "El viento máximo permitido es 40")
    private int windRequired;

    @NonNull
    @NotBlank(message = "La ubicación no puede estar vacia")
    private String location;

    @NonNull
    @NotBlank(message = "Tienes que especificar el dueño")
    private String owner;
}
