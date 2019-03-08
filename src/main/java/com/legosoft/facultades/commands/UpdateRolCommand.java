package com.legosoft.facultades.commands;

import com.legosoft.facultades.models.Permiso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRolCommand {

    @TargetAggregateIdentifier
    private Long idRolEvent;

    private String nombre;

    private Boolean isActivo;

}
