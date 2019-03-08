package com.legosoft.facultades.repository;

import com.legosoft.facultades.models.Rol;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolRepository extends Neo4jRepository<Rol,Long> {

    @Override
    List<Rol> findAll();

}
