package com.legosoft.facultades.repository;

import com.legosoft.facultades.models.Perfil;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilRepository extends Neo4jRepository<Perfil,Long> {

    @Override
    List<Perfil> findAll();

    Perfil findByNombre(String nombre);

}
