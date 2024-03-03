package com.app.sistemaacademico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "full_name")
    @NotNull
    private String full_name;

    @Column(name = "dni")
    @NotNull
    private long dni;

    @Column(name = "phone")
    @NotNull
    private String phone;

}
