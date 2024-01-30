package com.sau.sau.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.sau.sau.models.Cliente;
import com.sau.sau.models.Pessoa;
import com.sau.sau.models.dtos.ClienteDTO;
import com.sau.sau.repository.ClienteRepository;
import com.sau.sau.repository.PessoaRepository;
import com.sau.sau.services.exceptions.DataIntegrityViolationException;
import com.sau.sau.services.exceptions.ObjectnotFoundException;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepository repository;
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Cliente buscaClienteId(Integer id) {
		Optional<Cliente> cliente = repository.findById(id);
		return cliente.orElseThrow(() -> new ObjectnotFoundException("Objeto não encontrado! Id: " + id));
	}
	
	public List<Cliente> listaClientes() {
		return repository.findAll();
	}
	
	public Cliente criarCliente(ClienteDTO objDTO) {
		objDTO.setId(null);
		objDTO.setSenha(objDTO.getSenha());
		validaPorCpfEEmail(objDTO);
		Cliente newObj = new Cliente(objDTO);
		return repository.save(newObj);
	}
	
	public Cliente atualizaCliente(Integer id, @Validated ClienteDTO clienteDto) {
		clienteDto.setId(id);
		Cliente cliente = buscaClienteId(id);
		
		if(!clienteDto.getSenha().equals(cliente.getSenha())) 
			clienteDto.setSenha(clienteDto.getSenha());
		
		validaPorCpfEEmail(clienteDto);
		cliente = new Cliente(clienteDto);
		return repository.save(cliente);
	}

	public void apagaCliente(Integer id) {
		Cliente cliente = buscaClienteId(id);

		if (cliente.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Cliente possui ordens de serviço e não pode ser deletado!");
		}

		repository.deleteById(id);
	}
	
	//Verifica se o email ou cpf ja estam cadastrados no sistema.
	private void validaPorCpfEEmail(ClienteDTO clienteDto) {
		Optional<Pessoa> cliente = pessoaRepository.findByCpf(clienteDto.getCpf());
		if (cliente.isPresent() && cliente.get().getId() != clienteDto.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}

		cliente = pessoaRepository.findByEmail(clienteDto.getEmail());
		if (cliente.isPresent() && cliente.get().getId() != clienteDto.getId()) {
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
		}
	}
}
