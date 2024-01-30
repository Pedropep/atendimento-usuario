package com.sau.sau.services;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.JavaServiceLoadable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.sau.sau.models.Pessoa;
import com.sau.sau.models.Tecnico;
import com.sau.sau.models.dtos.TecnicoDTO;
import com.sau.sau.repository.PessoaRepository;
import com.sau.sau.repository.TecnicoRepository;
import com.sau.sau.services.exceptions.DataIntegrityViolationException;
import com.sau.sau.services.exceptions.ObjectnotFoundException;
@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository repository;
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Tecnico buscaTecnicoId(Integer id) {
		Optional<Tecnico> tecnico = repository.findById(id);
		return tecnico.orElseThrow(() -> new ObjectnotFoundException("Objeto não encontrado! Id: " + id));
	}
	
	public List<Tecnico> bucaTodosTecnicos() {
		return repository.findAll();
	}
	
	public Tecnico criaTecnico(TecnicoDTO tecnicoDto) {
		tecnicoDto.setId(null);
		tecnicoDto.setSenha(tecnicoDto.getSenha());
		validaPorCpfEEmail(tecnicoDto);
		Tecnico newObj = new Tecnico(tecnicoDto);
		return repository.save(newObj);
	}
	
	public Tecnico atualizaTecnico(Integer id, @Validated TecnicoDTO tecnicoDto) {
		tecnicoDto.setId(id);
		Tecnico tecnico = buscaTecnicoId(id);
		
		if(!tecnicoDto.getSenha().equals(tecnico.getSenha())) 
			tecnicoDto.setSenha(tecnicoDto.getSenha());
		
		validaPorCpfEEmail(tecnicoDto);
		tecnico = new Tecnico(tecnicoDto);
		return repository.save(tecnico);
	}	
	
	//Apaga um tecnico se ele não tiver com nenhum chamado na caixa.
	public void apagaTecnico(Integer id) {
		Tecnico tecnico = buscaTecnicoId(id);

		if (tecnico.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado!");
		}

		repository.deleteById(id);
	}	

	//Verifica se o email ou cpf ja estam cadastrados no sistema.
	private void validaPorCpfEEmail(TecnicoDTO objDTO) {
		Optional<Pessoa> pessoa = pessoaRepository.findByCpf(objDTO.getCpf());
		if (pessoa.isPresent() && pessoa.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}

		pessoa = pessoaRepository.findByEmail(objDTO.getEmail());
		if (pessoa.isPresent() && pessoa.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
		}
	}
}
