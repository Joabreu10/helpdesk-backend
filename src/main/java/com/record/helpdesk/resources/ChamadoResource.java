package com.record.helpdesk.resources;

import com.record.helpdesk.domain.Chamado;
import com.record.helpdesk.domain.dtos.ChamadoDTO;
import com.record.helpdesk.services.ChamadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/chamados")
public class ChamadoResource {

    @Autowired
    ChamadoService chamadoService;

    @GetMapping(value = "/{id}")
    public ResponseEntity <ChamadoDTO> FindById (@PathVariable Integer id){
        Chamado obj = chamadoService.findById(id);
        return ResponseEntity.ok().body(new ChamadoDTO(obj));
    }

    @GetMapping
    public ResponseEntity <List<ChamadoDTO>> FindAll(){
        List<Chamado> list = chamadoService.findAll();
        List<ChamadoDTO> listDTO = list.stream().map( obj -> new ChamadoDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    @PostMapping
    public ResponseEntity <ChamadoDTO> create(@Valid @RequestBody ChamadoDTO objDTO){
        Chamado obj = chamadoService.create(objDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().
                path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity <ChamadoDTO> update (@PathVariable Integer id,@Valid @RequestBody ChamadoDTO objDto){
        Chamado newObj = chamadoService.update(id, objDto);
        return ResponseEntity.ok().body(new ChamadoDTO(newObj));
    }

}
