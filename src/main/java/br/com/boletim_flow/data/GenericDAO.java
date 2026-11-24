package br.com.boletim_flow.data;

import java.util.List;
import java.util.Optional;

import br.com.boletim_flow.entity.Aluno;

public interface GenericDAO<T> {
	List<T> buscarTodos();
	Optional<T> buscarPorNome(String nome);
}
