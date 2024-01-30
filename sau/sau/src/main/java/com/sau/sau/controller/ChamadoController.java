package com.sau.sau.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sau.sau.models.Chamado;
import com.sau.sau.models.dtos.ChamadosDTO;
import com.sau.sau.services.ChamadoService;

@RestController
@RequestMapping(value = "/chamados")
public class ChamadoController {
	
	@Autowired
	private ChamadoService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<ChamadosDTO> buscaPorId(@PathVariable Integer id) {
		Chamado chamado = service.buscaChamadoId(id);
		return ResponseEntity.ok().body(new ChamadosDTO(chamado));
	}

	@GetMapping
	public ResponseEntity<List<ChamadosDTO>> buscaTodos() {
		List<Chamado> list = service.buscaChamados();
		List<ChamadosDTO> listDTO = list.stream().map(obj -> new ChamadosDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}

	@PostMapping
	public ResponseEntity<ChamadosDTO> criaChamado(@Validated @RequestBody ChamadosDTO obj) {
		Chamado newObj = service.criaChamado(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ChamadosDTO> atualizaChamado(@PathVariable Integer id, @Validated @RequestBody ChamadosDTO objDTO) {
		Chamado newObj = service.update(id, objDTO);
		return ResponseEntity.ok().body(new ChamadosDTO(newObj));
	}
}
