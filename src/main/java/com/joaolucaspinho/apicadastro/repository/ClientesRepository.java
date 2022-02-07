package com.joaolucaspinho.apicadastro.repository;

import com.joaolucaspinho.apicadastro.model.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientesRepository
        extends JpaRepository<Clientes, Long> { }