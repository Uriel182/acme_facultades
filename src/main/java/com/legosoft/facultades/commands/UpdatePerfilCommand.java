package com.legosoft.facultades.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePerfilCommand {

    @TargetAggregateIdentifier
    private Long idPerfilEvent;

    private String nombre;

    private Boolean activo;

    private Boolean isAdmin;

}
