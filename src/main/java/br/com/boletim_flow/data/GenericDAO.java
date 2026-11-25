package br.com.boletim_flow.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {
	List<T> buscarTodos();
	Optional<T> buscarPorNome(String nome);
	T mapear(ResultSet rs) throws SQLException;
}
