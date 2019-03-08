package com.legosoft.facultades.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolCreatedEvent {

    private Long idRolEvent;

    private String nombre;

    private Boolean activo;

}
