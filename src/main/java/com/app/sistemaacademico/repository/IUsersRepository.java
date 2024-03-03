package com.app.sistemaacademico.repository;

import com.app.sistemaacademico.model.Users;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface IUsersRepository extends JpaRepository<Users, Integer> {

    Users findByDni(long dni);
    
    Optional<Users> findByUsername(String username);
    

}
