package com.legosoft.facultades.dtos;

import lombok.Data;

@Data
public class PermisoDto {

    private Long idPermiso;

    private String nombre;

    private Boolean permisoAcme;

    private String descripcion;

    private Boolean permisoInicioSesion;

    private Boolean activo;
}
