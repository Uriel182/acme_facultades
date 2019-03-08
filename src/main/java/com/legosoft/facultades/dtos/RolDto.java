package com.legosoft.facultades.dtos;

import com.legosoft.facultades.models.Permiso;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RolDto {

    private Long idRol;

    private String nombre;

    private Boolean activo;

    private Set<Permiso> permisos;

}
