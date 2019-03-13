package com.legosoft.facultades.Services;

import com.google.gson.Gson;
import com.legosoft.facultades.commands.CreateRolCommand;
import com.legosoft.facultades.models.Permiso;
import com.legosoft.facultades.models.Rol;
import com.legosoft.facultades.repository.PermisoRepository;
import com.legosoft.facultades.repository.RolRepository;
import com.legosoft.facultades.services.impl.RolServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class RolServiceTest {

    private static final Long       ID_PERMISO      = 182L;
    private static final Long       ID_ROL          = 200L;
    private static final Long       ID_EVENT        = 200l;
    private static final String     NOMBRE          = "RolService Test";
    private static final Boolean    ACTIVO          = true;
    private static final String     TIPO            = "rol";

    private RolServiceImpl      rolService;
    private RolRepository       rolRepository;
    private PermisoRepository   permisoRepository;
    private CommandGateway      commandGateway;
    private RabbitTemplate      rabbitTemplate;
    private CreateRolCommand    createRolCommand;

    private Permiso             permiso;
    private Optional<Permiso>   optionalPermiso;
    private Set<Permiso>        permisos = new HashSet<>();
    private Rol                 rolNuevo;
    private Rol                 rolGuardado;


    @Before
    public void setup(){

        rolRepository = Mockito.mock(RolRepository.class);
        permisoRepository = Mockito.mock(PermisoRepository.class);
        commandGateway = Mockito.mock(CommandGateway.class);
        rabbitTemplate = Mockito.mock(RabbitTemplate.class);

        rolService = new RolServiceImpl(commandGateway);
        rolService.setRolRepository(rolRepository);
        rolService.setPermisoRepository(permisoRepository);
        rolService.setRabbitTemplate(rabbitTemplate);

        permiso = new Permiso();
        permiso.setIdPermiso(ID_PERMISO);
        optionalPermiso = Optional.of(permiso);
        permisos.add(permiso);

        rolNuevo = new Rol(null,ID_EVENT,NOMBRE,permisos,ACTIVO,TIPO);
        rolGuardado = new Rol(ID_ROL,ID_EVENT,NOMBRE,permisos,ACTIVO,TIPO);
        createRolCommand = new CreateRolCommand(ID_ROL,NOMBRE,ACTIVO,permisos);

    }

    @Test
    public void shoulCreateRol(){

        log.info("Stubbing permisoRepository.findById({}) to return optionalPermiso",ID_PERMISO);
        Mockito.when(permisoRepository.findById(ID_PERMISO)).thenReturn(optionalPermiso);

        log.info("Stubbing permisoRepository.findById({}) to return optionalPermiso",ID_PERMISO);
        Mockito.when(rolRepository.save(rolNuevo)).thenReturn(rolGuardado);

        rolService.saveRol(rolNuevo);

        Mockito.verify(rolRepository).save(rolNuevo);

        Mockito.verify(rabbitTemplate).convertAndSend("ExchangeCQRS","*", new Gson().toJson(rolGuardado));

        Mockito.verify(commandGateway).sendAndWait(createRolCommand);

    }

}
