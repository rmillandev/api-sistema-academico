package com.app.sistemaacademico.repository;

import com.app.sistemaacademico.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IAdminRepository extends JpaRepository<Admin, Integer> {

}
