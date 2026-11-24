package br.com.boletim_flow.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.boletim_flow.entity.Aluno;
import br.com.boletim_flow.entity.Disciplina;
import br.com.boletim_flow.exception.DatabaseException;
import br.com.boletim_flow.utils.ConexaoBD;

public class DisciplinaDAO implements GenericDAO<Disciplina> {

	@Override
	public List<Disciplina> buscarTodos() {
		String query = "SELECT * FROM disciplina;";
		List<Disciplina> disciplinas = new ArrayList<>();
		
		try(PreparedStatement ps = ConexaoBD.getConnection().prepareStatement(query)){
			
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					Disciplina disciplina= mapearDisciplina(rs);
					disciplinas.add(disciplina);
				}
			}
			
		} catch (SQLException e) {
			System.err.println("[ERRO]: Erro ao buscar todos as disciplinas: " + e);
			e.printStackTrace();
			throw new DatabaseException("Erro interno no banco dados");
		}
		
		return disciplinas;
	}
	

	@Override
	public Optional<Disciplina> buscarPorNome(String nome) {
		String query = "SELECT * FROM disciplina WHERE nome = ?";
		
		try(PreparedStatement ps = ConexaoBD.getConnection().prepareStatement(query)){
			
			ps.setString(1, nome);
			
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) return Optional.of(mapearDisciplina(rs));
			}
			
		} catch (SQLException e) {
			System.err.println("[ERRO]: Erro ao buscar a disciplina de nome " + nome + ": " + e);
			e.printStackTrace();
			throw new DatabaseException("Erro interno no banco dados");
		}
		
		return Optional.empty();
	}
	
	
	private Disciplina mapearDisciplina(ResultSet rs) throws SQLException {
		return new Disciplina(
			rs.getLong("id"),
			rs.getString("nome"),
			rs.getInt("carga_horaria")
		);
	}
}
