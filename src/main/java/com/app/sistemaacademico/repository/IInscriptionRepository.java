package com.app.sistemaacademico.repository;

import com.app.sistemaacademico.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInscriptionRepository extends JpaRepository<Inscription, Integer> {
}
