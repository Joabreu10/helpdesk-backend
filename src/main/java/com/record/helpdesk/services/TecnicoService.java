package com.record.helpdesk.services;

import java.util.List;
import java.util.Optional;

import com.record.helpdesk.domain.Pessoa;
import com.record.helpdesk.repositories.PessoaRepository;
import com.record.helpdesk.services.exceptions.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.record.helpdesk.domain.Tecnico;
import com.record.helpdesk.domain.dtos.TecnicoDTO;
import com.record.helpdesk.repositories.TecnicoRepository;
import com.record.helpdesk.services.exceptions.ObjectnotFoundException;

import javax.validation.Valid;

@Service
public class TecnicoService {

	@Autowired
	private TecnicoRepository tecnicoRepository;
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj  = tecnicoRepository.findById(id);
		return obj.orElseThrow(( )-> new ObjectnotFoundException("Objeto não encontrado! Id: " + id) );
	}

	public List<Tecnico> findAll() {
		return tecnicoRepository.findAll();
	}

	public Tecnico create(TecnicoDTO objDTO) {
		objDTO.setId(null);
		validaPorCpfEEmail(objDTO);
		Tecnico newObj = new Tecnico(objDTO);
		return tecnicoRepository.save(newObj);
	}

	public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
		objDTO.setId(id);
		Tecnico oldObj = findById(id);
		validaPorCpfEEmail(objDTO);
		oldObj = new Tecnico(objDTO);
		return tecnicoRepository.save(oldObj);
	}

	public void delete(Integer id) {
		Tecnico obj = findById(id);
		if(obj.getChamados().size() > 0){
			throw new DataIntegrityViolationException("Técnico possui chamado ordens de serviço e não pode ser deletado");
		}
			tecnicoRepository.deleteById(id);

	}

	private void validaPorCpfEEmail(TecnicoDTO objDTO) {
		Optional <Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()){
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}

		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()){
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
		}
	}



}
