package com.legosoft.facultades.models;

import com.legosoft.facultades.commands.CreatePermisoCommand;
import com.legosoft.facultades.commands.DisablePermisoCommand;
import com.legosoft.facultades.commands.UpdatePermisoCommand;
import com.legosoft.facultades.events.PermisoCreatedEvent;
import com.legosoft.facultades.events.PermisoDisableEvent;
import com.legosoft.facultades.events.PermisoUpdateEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Aggregate
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@NodeEntity(label = "Permiso")
public class Permiso implements Serializable {

    @Id
    @GeneratedValue()
    private Long idPermiso;

    @Transient
    @AggregateIdentifier
    private Long idPermisoEvent;

    private String nombre;

    private Boolean permisoAcme;

    private String descripcion;

    private Boolean permisoInicioSesion;

    private Boolean activo;

    @Transient
    private String tipo;

    @CommandHandler
    public Permiso(CreatePermisoCommand command) {

        AggregateLifecycle.apply(new PermisoCreatedEvent(command.getIdPermiso(),command.getNombre(),command.getPermisoAcme(),
                command.getDescripcion(),command.getPermisoInicioSesion(),command.getIsActivo()));
    }

    @EventSourcingHandler
    protected void on(PermisoCreatedEvent event){
        this.idPermisoEvent = event.getIdPermisoEvent();
        this.nombre = event.getNombre();
        this.permisoAcme = event.getPermisoAcme();
        this.descripcion = event.getDescripcion();
        this.permisoInicioSesion = event.getPermisoInicioSesion();
        this.activo = event.getIsActivo();
    }

    @CommandHandler
    protected void on(UpdatePermisoCommand command){
        AggregateLifecycle.apply(new PermisoUpdateEvent(
               command.getIdPermisoEvent(),command.getNombre(),
                command.getPermisoAcme(),command.getDescripcion(),command.getPermisoInicioSesion(),command.getIsActivo()));
    }

    @EventSourcingHandler
    protected void on(PermisoUpdateEvent event){
        this.nombre = event.getNombre();
        this.permisoAcme = event.getPermisoAcme();
        this.descripcion = event.getDescripcion();
        this.permisoInicioSesion = event.getPermisoInicioSesion();
        this.activo = event.getIsActivo();
    }

    @CommandHandler
    protected void on(DisablePermisoCommand command){
        AggregateLifecycle.apply(new PermisoDisableEvent(
                command.getIdPermiso(),false
        ));
    }

    @EventSourcingHandler
    protected void on(PermisoDisableEvent event){
        this.activo = event.getActivo();
    }


}
