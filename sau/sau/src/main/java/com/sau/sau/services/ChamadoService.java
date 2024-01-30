package com.sau.sau.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.sau.sau.models.Chamado;
import com.sau.sau.models.Cliente;
import com.sau.sau.models.Tecnico;
import com.sau.sau.models.dtos.ChamadosDTO;
import com.sau.sau.models.enums.Prioridade;
import com.sau.sau.models.enums.Status;
import com.sau.sau.repository.ChamadoRepository;
import com.sau.sau.services.exceptions.ObjectnotFoundException;

@Service
public class ChamadoService {
	
	@Autowired
	private ChamadoRepository repository;
	
	@Autowired
	private TecnicoService tecnicoService;
	
	@Autowired
	private ClienteService clienteService;
	
	public Chamado buscaChamadoId(Integer id) {
		Optional<Chamado> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectnotFoundException("Objeto não encontrado! ID: " + id));
	}

	public List<Chamado> buscaChamados() {
		return repository.findAll();
	}

	public Chamado criaChamado(ChamadosDTO chamadoDto) {
		return repository.save(newChamado(chamadoDto));
	}

	public Chamado update(Integer id, @Validated ChamadosDTO chamadoDto) {
		chamadoDto.setId(id);
		Chamado chamado = buscaChamadoId(id);
		chamado = newChamado(chamadoDto);
		return repository.save(chamado);
	}

	private Chamado newChamado(ChamadosDTO obj) {
		Tecnico tecnico = tecnicoService.buscaTecnicoId(obj.getTecnico());
		Cliente cliente = clienteService.buscaClienteId(obj.getCliente());
		
		Chamado chamado = new Chamado();
		if(obj.getId() != null) {
			chamado.setId(obj.getId());
		}
		
		if(obj.getStatus().equals(2)) {
			chamado.setDataFechamento(LocalDate.now());
		}
		
		chamado.setTecnico(tecnico);
		chamado.setCliente(cliente);
		chamado.setPrioridade(Prioridade.toEnum(obj.getPrioridade()));
		chamado.setStatus(Status.toEnum(obj.getStatus()));
		chamado.setTitulo(obj.getTitulo());
		chamado.setObservacoes(obj.getObservacoes());
		return chamado;
	}
}
