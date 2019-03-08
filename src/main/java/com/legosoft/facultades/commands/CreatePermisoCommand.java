package com.legosoft.facultades.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.model.AggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePermisoCommand {

    @AggregateIdentifier
    private Long idPermiso;

    private String nombre;

    private Boolean permisoAcme;

    private String descripcion;

    private Boolean permisoInicioSesion;

    private Boolean isActivo;

}
