package com.legosoft.facultades.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePermisoCommand {

    @TargetAggregateIdentifier
    private Long idPermisoEvent;

    private String nombre;

    private Boolean permisoAcme;

    private String descripcion;

    private Boolean permisoInicioSesion;

    private Boolean isActivo;
}
