package com.legosoft.facultades.services.impl;

import com.google.gson.Gson;
import com.legosoft.facultades.commands.CreateRolCommand;
import com.legosoft.facultades.commands.DisableRolCommand;
import com.legosoft.facultades.commands.UpdateRolCommand;
import com.legosoft.facultades.models.Permiso;
import com.legosoft.facultades.models.Rol;
import com.legosoft.facultades.repository.PermisoRepository;
import com.legosoft.facultades.repository.RolRepository;
import com.legosoft.facultades.services.RolService;
import lombok.Data;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Data
@Service("rolService")
public class RolServiceImpl implements RolService {

    private static final String TIPO = "rol";

    private CommandGateway commandGateway;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    public RolServiceImpl(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }

    @Override
    public CompletableFuture<String> saveRol(Rol rol) {

        Set<Permiso> permisos = new HashSet<>();

        rol.getPermisos().stream().forEach(p -> {
            permisos.add(permisoRepository.findById(p.getIdPermiso()).get());
        });

        rol.setPermisos(permisos);
        rol.setTipo(TIPO);

        Rol r = rolRepository.save(rol);

        CreateRolCommand command = new CreateRolCommand(r.getIdRol(),r.getNombre(),r.getActivo(),r.getPermisos());

        rabbitTemplate.convertAndSend("ExchangeCQRS","*", new Gson().toJson(r));

        return commandGateway.sendAndWait(command);

    }

    @Transactional
    @Override
    public CompletableFuture<String> updateRol(Rol rol) {

        Rol r = rolRepository.findById(rol.getIdRol()).get();

        rolRepository.save(rol);

        UpdateRolCommand command = new UpdateRolCommand(r.getIdRol(),rol.getNombre(),rol.getActivo());

        return commandGateway.send(command);

    }

    @Override
    public CompletableFuture<String> deshabilitarRol(Rol rol) {

        Rol r = rolRepository.findById(rol.getIdRol()).get();

        rol.setActivo(false);

        rolRepository.save(r);

        DisableRolCommand command = new DisableRolCommand(r.getIdRol());

        return commandGateway.send(command);

    }

    @Override
    public void findRoleById(Long roleId) {


    }

    @Override
    public void findAll() {


    }

}
