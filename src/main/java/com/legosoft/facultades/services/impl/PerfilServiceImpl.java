package com.legosoft.facultades.services.impl;

import com.google.gson.Gson;
import com.legosoft.facultades.commands.CreatePerfilCommand;
import com.legosoft.facultades.commands.DisablePerfilCommand;
import com.legosoft.facultades.commands.UpdatePerfilCommand;
import com.legosoft.facultades.models.Perfil;
import com.legosoft.facultades.models.Rol;
import com.legosoft.facultades.repository.PerfilRepository;
import com.legosoft.facultades.repository.RolRepository;
import com.legosoft.facultades.services.PerfilService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.Perf;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service("perfilService")
public class PerfilServiceImpl implements PerfilService {

    private static final String TIPO = "perfil";

    private CommandGateway commandGateway;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private RolRepository rolRepository;

    public PerfilServiceImpl(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }

    @Override
    public CompletableFuture<String> savePefil(Perfil perfil) {

        Perfil per = perfilRepository.findByNombre(perfil.getNombre());

        if (Objects.isNull(per)){
            Set<Rol> roles = new HashSet<>();

            perfil.getRolesPerfil().stream().forEach(r ->
                    roles.add(rolRepository.findById(r.getIdRol()).get())
            );

            perfil.setRolesPerfil(roles);

            Perfil p = perfilRepository.save(perfil);

            perfil.setTipo(TIPO);

            CreatePerfilCommand command = new CreatePerfilCommand(
                    p.getIdPerfil(),p.getNombre(),
                    p.getActivo(),p.getIsAdmin(),
                    p.getRolesPerfil()
            );

            rabbitTemplate.convertAndSend("facultades","*", new Gson().toJson(command));
            rabbitTemplate.convertAndSend("ExchangeCQRS","*", new Gson().toJson(perfil));

            return commandGateway.send(command);
        } else {
            throw new IllegalArgumentException("El nombre del Perfil ya existe");
        }

    }

    @Override
    public CompletableFuture<String> updatePerfil(Perfil perfil) {

        Perfil p = perfilRepository.findById(perfil.getIdPerfil()).get();

        UpdatePerfilCommand command = new UpdatePerfilCommand(
                p.getIdPerfil(),perfil.getNombre(),
                perfil.getActivo(),perfil.getIsAdmin()
        );

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> deshabilitarPerfil(Perfil perfil) {

        Perfil p = perfilRepository.findById(perfil.getIdPerfil()).get();

        DisablePerfilCommand command = new DisablePerfilCommand(p.getIdPerfil());

        return commandGateway.send(command);

    }

    @Override
    public void findPerfilById(Long perfilId) {

    }

    @Override
    public void findAll() {

    }
}
