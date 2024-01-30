package com.sau.sau.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sau.sau.models.Tecnico;
import com.sau.sau.models.dtos.TecnicoDTO;
import com.sau.sau.services.TecnicoService;

@RestController
@RequestMapping(value = "/tecnicos")
public class TecnicoController {
	
	@Autowired
	private TecnicoService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> findById(@PathVariable Integer id) {
		Tecnico obj = service.buscaTecnicoId(id);
		return ResponseEntity.ok().body(new TecnicoDTO(obj));
	}

	@GetMapping
	public ResponseEntity<List<TecnicoDTO>> findAll() {
		List<Tecnico> lista = service.bucaTodosTecnicos();
		List<TecnicoDTO> listDTO = lista.stream().map(obj -> new TecnicoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}

//	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<TecnicoDTO> create(@Validated @RequestBody TecnicoDTO tecnicoDto) {
		Tecnico newTecnico = service.criaTecnico(tecnicoDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newTecnico.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
//	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> update(@PathVariable Integer id, @Validated @RequestBody TecnicoDTO tecnicoDto) {
		Tecnico tecnico = service.atualizaTecnico(id, tecnicoDto);
		return ResponseEntity.ok().body(new TecnicoDTO(tecnico));
	}
	
//	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> delete(@PathVariable Integer id) {
		service.apagaTecnico(id); 
		return ResponseEntity.noContent().build();
	}
}
