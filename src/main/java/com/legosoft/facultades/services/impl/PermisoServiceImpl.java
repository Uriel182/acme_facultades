package com.legosoft.facultades.services.impl;

import com.google.gson.Gson;
import com.legosoft.facultades.commands.CreatePermisoCommand;
import com.legosoft.facultades.commands.DisablePermisoCommand;
import com.legosoft.facultades.commands.UpdatePermisoCommand;
import com.legosoft.facultades.models.Permiso;
import com.legosoft.facultades.repository.PermisoRepository;
import com.legosoft.facultades.services.PermisoService;
import lombok.Data;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Data
@Service("permisoService")
public class PermisoServiceImpl implements PermisoService {

    private static final String TIPO_PERMISO = "permiso";

    private CommandGateway commandGateway;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PermisoRepository permisoRepository;

    public PermisoServiceImpl(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }


    public CompletableFuture<String> savePermiso(Permiso permiso){

        if (Objects.isNull(permisoRepository.findByNombre(permiso.getNombre()))){

            Permiso per = permisoRepository.save(permiso);

            per.setTipo(TIPO_PERMISO);

        CreatePermisoCommand command = new CreatePermisoCommand(per.getIdPermiso(),per.getNombre(), per.getPermisoAcme(),
                per.getDescripcion(),per.getPermisoInicioSesion(),per.getActivo());

            rabbitTemplate.convertAndSend("ExchangeCQRS","*", new Gson().toJson(per));

            return commandGateway.send(command);

        } else {

            throw new IllegalArgumentException("El nombre del Permiso ya existe");

        }


    }

    @Override
    public CompletableFuture<String> updatePermiso(Permiso permiso) {

        Permiso p = permisoRepository.findById(permiso.getIdPermiso()).orElse(null);

        if (Objects.nonNull(p)){

            if (Objects.isNull(permisoRepository.findByNombre(permiso.getNombre()))){
                permisoRepository.save(permiso);
            } else {
                throw new IllegalArgumentException("El nombre del Permiso ya existe");
            }

            UpdatePermisoCommand command = new UpdatePermisoCommand(p.getIdPermiso(),permiso.getNombre(),permiso.getPermisoAcme(),
                    permiso.getDescripcion(),permiso.getPermisoInicioSesion(),permiso.getActivo());

            rabbitTemplate.convertAndSend("ExchangeCQRS","*",new Gson().toJson(permiso));

            return commandGateway.sendAndWait(command);

        }else {

            throw new IllegalArgumentException("El Permiso que se quiere actualizar no existe");

        }


    }

    @Override
    public CompletableFuture<String> deshabilitarPermiso(Permiso permiso) {

        Permiso p = permisoRepository.findById(permiso.getIdPermiso()).orElse(null);

        if (Objects.nonNull(p)){

            p.setActivo(false);

            permisoRepository.save(p);

            DisablePermisoCommand command = new DisablePermisoCommand(p.getIdPermiso());

            return commandGateway.sendAndWait(command);

        }else {

            throw new IllegalArgumentException("El Permiso que se quiere deshabilitar no existe");

        }



    }

    @Override
    public Permiso findPermisobyId(Long idPermiso) {

        Permiso permiso = permisoRepository.findById(idPermiso).orElse(null);

        if (Objects.nonNull(permiso)){
            return permiso;
        }else {
            throw new IllegalArgumentException("El Permiso no existe");
        }

    }

    @Override
    public List<Permiso> findAll() {

        return permisoRepository.findAll();

    }
}
