package com.legosoft.facultades.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilCreatedEvent {

    private Long idPerfil;

    private String nombre;

    private Boolean activo;

    private Boolean isAdmin;

}
