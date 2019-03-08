package com.legosoft.facultades.models;

import com.legosoft.facultades.commands.CreatePerfilCommand;
import com.legosoft.facultades.commands.DisablePerfilCommand;
import com.legosoft.facultades.commands.UpdatePerfilCommand;
import com.legosoft.facultades.events.PerfilCreatedEvent;
import com.legosoft.facultades.events.PerfilDisableEvent;
import com.legosoft.facultades.events.PerfilUpdateEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Aggregate
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@NodeEntity(label = "Perfil")
public class Perfil {

    @Id
    @GeneratedValue()
    private Long idPerfil;

    @Transient
    @AggregateIdentifier
    private Long idPerfilEvent;

    private String nombre;

    private Boolean activo;

    private Boolean isAdmin;

    @Transient
    private String tipo;

    @Relationship(type = "HAS_ROL")
    private Set<Rol> rolesPerfil = new HashSet<>();

    @CommandHandler
    public Perfil(CreatePerfilCommand command){

        AggregateLifecycle.apply(new PerfilCreatedEvent(
                command.getIdPerfil(), command.getNombre(),
                command.getActivo(),command.getIsAdmin()
        ));
    }

    @EventSourcingHandler
    protected void on(PerfilCreatedEvent event){
        this.idPerfilEvent = event.getIdPerfil();
        this.nombre = event.getNombre();
        this.activo = event.getActivo();
        this.isAdmin = event.getIsAdmin();
    }

    @CommandHandler
    protected void on(UpdatePerfilCommand command){

        AggregateLifecycle.apply(new PerfilUpdateEvent(
                command.getIdPerfilEvent(), command.getNombre(),
                command.getActivo(),command.getIsAdmin()
        ));

    }

    @EventSourcingHandler
    protected void on(PerfilUpdateEvent event){
        this.idPerfilEvent = event.getIdPerfilEvent();
        this.nombre = event.getNombre();
        this.activo = event.getActivo();
        this.isAdmin = event.getIsAdmin();
    }

    @CommandHandler
    protected void on(DisablePerfilCommand command){

        AggregateLifecycle.apply(new PerfilDisableEvent(
                command.getIdPermiso(),false
        ));

    }

    @EventSourcingHandler
    protected void on(PerfilDisableEvent event){
        this.activo = event.getActivo();
    }
}
