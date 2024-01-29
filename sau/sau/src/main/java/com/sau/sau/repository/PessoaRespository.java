package com.sau.sau.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sau.sau.models.Pessoa;

public interface PessoaRespository extends JpaRepository<Pessoa, Integer> {
	public Optional<Pessoa> findByCpf(String cpf);
	public Optional<Pessoa> findByEmail(String email);
}
