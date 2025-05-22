package com.iron.kite_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "kites")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Kite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private int windRequired;

    @NonNull
    private String location;

    @NonNull
    private String owner;
}
