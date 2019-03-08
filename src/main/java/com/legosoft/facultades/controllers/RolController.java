package com.legosoft.facultades.controllers;

import com.legosoft.facultades.dtos.RolDto;
import com.legosoft.facultades.models.Rol;
import com.legosoft.facultades.services.RolService;
import com.legosoft.facultades.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequestMapping("/roles")
@RestController
@Slf4j
public class RolController {

    @Autowired
    private RolService rolService;

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/creaRol")
    public void creaRol(@RequestBody RolDto request){

        log.info("Creando Rol: {}",request);

        Rol rol = new Rol();
        BeanUtils.copyProperties(request,rol);

        rolService.saveRol(rol);

    }

    /**
     *
     * @param request
     */
    @PutMapping("/actualizaRol")
    public void actualizaRol(@RequestBody RolDto request){

        log.info("Actualizando Rol: {}", request);

        Rol rol = new Rol();
        BeanUtils.copyProperties(request,rol);

        rolService.updateRol(rol);

    }

    @PutMapping("/deshabilitaRol")
    public void deshabilitaRol(@RequestBody Rol request){

    }

    @GetMapping("/findRolById/{idRole}")
    public void findRolById(@PathVariable("idRole") Long idRole){


    }

    @GetMapping("/findAll")
    public void findAll(){


    }

}
