package br.com.boletim_flow.data;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {
	List<T> buscarTodos();
	Optional<T> buscarPorId(long id);
}
