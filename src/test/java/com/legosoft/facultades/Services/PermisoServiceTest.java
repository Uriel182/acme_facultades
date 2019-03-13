package com.legosoft.facultades.Services;

import com.google.gson.Gson;
import com.legosoft.facultades.commands.CreatePermisoCommand;
import com.legosoft.facultades.commands.DisablePermisoCommand;
import com.legosoft.facultades.commands.UpdatePermisoCommand;
import com.legosoft.facultades.models.Permiso;
import com.legosoft.facultades.repository.PermisoRepository;
import com.legosoft.facultades.services.impl.PermisoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;

@Slf4j
public class PermisoServiceTest {

    private static final Long       ID              = 182L;
    private static final Long       ID_EVENT        = 100L;
    private static final String     NOMBRE          = "PermisoService test";
    private static final Boolean    PERMISO_ACME    = Boolean.TRUE;
    private static final String     DESCRIPCION     = "Test savePermiso de PermisoService";
    private static final String     DESC_UPDATE     = "Test updatePermiso de PermisoService";
    private static final Boolean    PERMISO_INICIO  = Boolean.TRUE;
    private static final Boolean    ACTIVO          = Boolean.TRUE;
    private static final String     TIPO            = "permiso";

    private PermisoServiceImpl permisoService;
    private PermisoRepository permisoRepository;
    private CommandGateway commandGateway;
    private RabbitTemplate rabbitTemplate;
    private CreatePermisoCommand createPermisoCommand;
    private UpdatePermisoCommand updatePermisoCommand;
    private DisablePermisoCommand disablePermisoCommand;

    private Permiso permisoNuevo;
    private Permiso permisoGuardado;
    private Optional<Permiso> optionalGuardado;
    private Permiso permisoActualizado;

    @Before
    public void setup(){

        permisoRepository = Mockito.mock(PermisoRepository.class);
        commandGateway = Mockito.mock(CommandGateway.class);
        rabbitTemplate = Mockito.mock(RabbitTemplate.class);

        permisoService = new PermisoServiceImpl(commandGateway);
        permisoService.setPermisoRepository(permisoRepository);
        permisoService.setRabbitTemplate(rabbitTemplate);

        permisoNuevo = new Permiso(null,ID_EVENT,NOMBRE,PERMISO_ACME,DESCRIPCION,PERMISO_INICIO,ACTIVO,TIPO);
        permisoGuardado = new Permiso(ID,ID_EVENT,NOMBRE,PERMISO_ACME,DESCRIPCION,PERMISO_INICIO,ACTIVO,TIPO);
        permisoActualizado = new Permiso(ID,ID_EVENT,NOMBRE,PERMISO_ACME,DESC_UPDATE,PERMISO_INICIO,ACTIVO,TIPO);

        optionalGuardado = Optional.of(permisoGuardado);

        createPermisoCommand = new CreatePermisoCommand(permisoGuardado.getIdPermiso(),permisoGuardado.getNombre(),permisoGuardado.getPermisoAcme(),
                permisoGuardado.getDescripcion(),permisoGuardado.getPermisoInicioSesion(),permisoGuardado.getActivo());

        updatePermisoCommand = new UpdatePermisoCommand(permisoGuardado.getIdPermiso(),permisoActualizado.getNombre(),permisoActualizado.getPermisoAcme(),
                permisoActualizado.getDescripcion(),permisoActualizado.getPermisoInicioSesion(),permisoActualizado.getActivo());

        disablePermisoCommand = new DisablePermisoCommand(permisoGuardado.getIdPermiso());

    }

    @Test
    public void shoulCreatePermiso(){

        log.info("Stubbing permisoRepository.findByNombre({}) to return NULL",NOMBRE);
        Mockito.when(permisoRepository.findByNombre(NOMBRE)).thenReturn(null);

        log.info("Stubbing permisoRepository.save(permiso) to return {}",permisoGuardado);
        Mockito.when(permisoRepository.save(permisoNuevo)).thenReturn(permisoGuardado);

        permisoService.savePermiso(permisoNuevo);

        Mockito.verify(permisoRepository).findByNombre(permisoNuevo.getNombre());

        Mockito.verify(permisoRepository).save(permisoNuevo);

        Mockito.verify(rabbitTemplate).convertAndSend("ExchangeCQRS","*", new Gson().toJson(permisoGuardado));

        Mockito.verify(commandGateway).send(createPermisoCommand);

    }

    @Test
    public void shoulUpdatePermiso(){

        log.info("Stubbing permisoRepository.findById({}) to return permisoGuardado",permisoGuardado.getIdPermiso());
        Mockito.when(permisoRepository.findById(permisoActualizado.getIdPermiso())).thenReturn(optionalGuardado);

        permisoService.updatePermiso(permisoActualizado);

        Mockito.verify(permisoRepository).findById(permisoGuardado.getIdPermiso());

        Mockito.verify(permisoRepository).save(permisoActualizado);

        Mockito.verify(rabbitTemplate).convertAndSend("ExchangeCQRS","*", new Gson().toJson(permisoActualizado));

        Mockito.verify(commandGateway).sendAndWait(updatePermisoCommand);

    }

    @Test
    public void shouldDisablePermiso(){

        log.info("Stubbing permisoRepository.findById({}) to return permisoGuardado",permisoGuardado.getIdPermiso());
        Mockito.when(permisoRepository.findById(permisoGuardado.getIdPermiso())).thenReturn(optionalGuardado);

        permisoService.deshabilitarPermiso(permisoGuardado);

        permisoGuardado.setActivo(false);

        Mockito.verify(permisoRepository).save(permisoGuardado);

        Mockito.verify(commandGateway).sendAndWait(disablePermisoCommand);


    }

}
