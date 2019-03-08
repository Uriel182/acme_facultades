package com.legosoft.facultades.controllers;

import com.legosoft.facultades.dtos.PerfilDto;
import com.legosoft.facultades.models.Perfil;
import com.legosoft.facultades.services.PerfilService;
import com.legosoft.facultades.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.misc.Perf;

import java.util.List;
import java.util.Objects;

@RequestMapping("/perfiles")
@RestController
@Slf4j
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    /**
     * Metodo que expone un servicio rest para guardar o actualizar un perfil.
     *
     * @param request
     * @return
     */
    @PostMapping("/creaPerfil")
    public void creaPerfil(@RequestBody PerfilDto request){

        log.info("Creando Perfil: {}",request);

        Perfil perfil = new Perfil();
        BeanUtils.copyProperties(request,perfil);

        perfilService.savePefil(perfil);

    }

    @PutMapping("/actualizaPerfil")
    public void actualizaPerfil(@RequestBody Perfil request){

        log.info("Actualizando: {}", request);

        Perfil perfil = new Perfil();
        BeanUtils.copyProperties(request,perfil);

        perfilService.updatePerfil(perfil);
    }


    @PutMapping("/deshabilitaPerfil")
    public void deshabilitaPerfil(@RequestBody Perfil request){

        log.info("Deshabilitando Perfil: {}", request);

        Perfil perfil = new Perfil();
        BeanUtils.copyProperties(request,perfil);

        perfilService.deshabilitarPerfil(perfil);
    }

    @GetMapping("/findPerfilById/{idPerfil}")
    public void findPerfilById(@PathVariable("idPerfil") Long idPerfil){


    }

    @GetMapping("/findAll")
    public void findAll(){


    }

}
