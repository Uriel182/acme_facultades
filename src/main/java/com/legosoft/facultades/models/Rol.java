package com.legosoft.facultades.models;

import com.legosoft.facultades.commands.CreateRolCommand;
import com.legosoft.facultades.commands.DisablePermisoCommand;
import com.legosoft.facultades.commands.UpdateRolCommand;
import com.legosoft.facultades.events.RolCreatedEvent;
import com.legosoft.facultades.events.RolDisableEvent;
import com.legosoft.facultades.events.RolUpdateEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Aggregate
@Data
@AllArgsConstructor
@NoArgsConstructor
@NodeEntity(label = "Rol")
public class Rol {

    @Id
    @GeneratedValue()
    private Long idRol;

    @Transient
    @AggregateIdentifier
    private Long idRolEvent;

    private String nombre;

    @Relationship(type = "HAS_PERMISO")
    private Set<Permiso> permisos = new HashSet<>();

    private Boolean activo;

    @Transient
    private String tipo;

    @CommandHandler
    public Rol(CreateRolCommand command){

        AggregateLifecycle.apply(new RolCreatedEvent(command.getIdRol(),command.getNombre(),
                command.getIsActivo()));
    }

    @EventSourcingHandler
    protected void on(RolCreatedEvent event){
        this.idRolEvent = event.getIdRolEvent();
        this.nombre = event.getNombre();
        this.activo = event.getActivo();
    }

    @CommandHandler
    protected void on(UpdateRolCommand command){

        AggregateLifecycle.apply(new RolUpdateEvent(
                command.getIdRolEvent(),
                command.getNombre(),command.getIsActivo()
        ));
    }

    @EventSourcingHandler
    protected void on(RolUpdateEvent event){
        this.nombre = event.getNombre();
        this.activo = event.getActivo();
    }

    @CommandHandler
    protected void on(DisablePermisoCommand command){

        AggregateLifecycle.apply(new RolDisableEvent(
                command.getIdPermiso(),false
                ));
    }

    @EventSourcingHandler
    protected void on(RolDisableEvent event){
        this.activo = event.getActivo();
    }

}
