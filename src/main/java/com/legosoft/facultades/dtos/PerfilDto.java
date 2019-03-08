package com.legosoft.facultades.dtos;

import lombok.Data;
import com.legosoft.facultades.models.Rol;

import java.util.Set;

@Data
public class PerfilDto {

    private String nombre;

    private Boolean activo;

    private Boolean isAdmin;

    private Set<Rol> rolesPerfil;

}
