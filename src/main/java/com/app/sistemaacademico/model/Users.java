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
@Table(name = "users",  uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username", unique = true)
    @NotNull
    private String username;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "full_name")
    @NotNull
    private String full_name;

    @Column(name = "dni")
    @NotNull
    private long dni;

    @Column(name = "role")
    @NotNull
    private String role;

}
