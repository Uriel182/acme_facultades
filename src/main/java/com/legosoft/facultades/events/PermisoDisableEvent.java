package com.legosoft.facultades.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermisoDisableEvent {

    private Long idPermiso;

    private Boolean activo;

}
