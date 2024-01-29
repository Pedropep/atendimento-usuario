package com.sau.sau.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sau.sau.models.Chamado;

public interface ChamadoRespository extends JpaRepository<Chamado, Integer>{
	
}
