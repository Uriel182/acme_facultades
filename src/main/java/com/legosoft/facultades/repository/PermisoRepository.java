package com.legosoft.facultades.repository;

import com.legosoft.facultades.models.Permiso;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermisoRepository extends Neo4jRepository<Permiso,Long> {

    @Override
    List<Permiso> findAll();

    Permiso findByNombre(String nombre);

    Permiso findByIdPermiso(Long idPermiso);

}
