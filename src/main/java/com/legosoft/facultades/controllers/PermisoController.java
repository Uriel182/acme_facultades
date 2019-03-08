package com.legosoft.facultades.controllers;

import com.legosoft.facultades.dtos.PermisoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.legosoft.facultades.models.Permiso;
import com.legosoft.facultades.services.PermisoService;
import com.legosoft.facultades.utils.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequestMapping("/permisos")
@RestController
public class PermisoController {

    public static final Logger logger = LoggerFactory.getLogger(PermisoController.class);

    @Autowired
    private PermisoService permisoService;


    /**
     * Metodo que expone un servicio rest para guardar un permiso.
     *
     * @param request
     * @return
     */
    @PostMapping("/creaPermiso")
    public void creaPermiso(@RequestBody PermisoDto request){

        logger.info("Creando Permiso: {}", request);

        Permiso p = new Permiso();
        BeanUtils.copyProperties(request, p);

        permisoService.savePermiso(p);

    }

    /**
     * Metodo que expone un servicio rest para actualizar un permiso.
     *
     * @param request
     */
    @PutMapping("/actualizaPermiso")
    public void actualizaPermiso(@RequestBody PermisoDto request){

        logger.info("Actualizando Permiso: {}", request);

        Permiso p = new Permiso();
        BeanUtils.copyProperties(request,p);

        permisoService.updatePermiso(p);
    }

    /**
     * Metodo que expone un servicio rest para deshabilitar un permiso
     *
     * @param request
     * @return
     */
    @PutMapping("/deshabilitarPermiso")
    public void deshabilitarPermiso(@RequestBody PermisoDto request){

        logger.info("Deshabilitando Permiso: {}", request);

        Permiso p = new Permiso();
        BeanUtils.copyProperties(request,p);

        permisoService.deshabilitarPermiso(p);
    }

    /**
     * Metodo que expone un servicio rest para buscar un permiso por su id
     *
     * @param idPermiso
     * @return
     */
    @GetMapping("/findPermisoById/{idPermiso}")
    public void findPermisoById(@PathVariable("idPermiso") Long idPermiso){


    }

    /**
     * Metodo que expone un servicio rest para buscar todos los permisos
     *
     * @return
     */
    @GetMapping("/findAllPermisos")
    public void findAll(){


    }
}
