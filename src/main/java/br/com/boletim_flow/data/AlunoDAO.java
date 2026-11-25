package br.com.boletim_flow.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.boletim_flow.entity.Aluno;
import br.com.boletim_flow.exception.DatabaseException;
import br.com.boletim_flow.utils.ConexaoBD;

public class AlunoDAO implements GenericDAO<Aluno> {

	@Override
	public List<Aluno> buscarTodos() {
		String query = "SELECT * FROM aluno;";
		List<Aluno> alunos = new ArrayList<>();
		
		try(PreparedStatement ps = ConexaoBD.getConnection().prepareStatement(query)){
			
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					Aluno aluno = mapear(rs);
					alunos.add(aluno);
				}
			}
			
		} catch (SQLException e) {
			System.err.println("[ERRO]: Erro ao buscar todos os alunos: " + e);
			e.printStackTrace();
			throw new DatabaseException("Erro interno no banco dados");
		}
		
		return alunos;
	}
	
	@Override
	public Optional<Aluno> buscarPorId(long id) {
		String query = "SELECT * FROM aluno WHERE id = ?";
		
		try(PreparedStatement ps = ConexaoBD.getConnection().prepareStatement(query)){
			
			ps.setLong(1, id);
			
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) return Optional.of(mapear(rs));
			}
			
		} catch (SQLException e) {
			System.err.println("[ERRO]: Erro ao buscar aluno de ID " + id + ": " + e);
			e.printStackTrace();
			throw new DatabaseException("Erro interno no banco dados");
		}
		
		return Optional.empty();
	}

	
	@Override
	public Optional<Aluno> buscarPorNome(String nome) {
		String query = "SELECT * FROM aluno WHERE nome = ?";
		
		try(PreparedStatement ps = ConexaoBD.getConnection().prepareStatement(query)){
			
			ps.setString(1, nome);
			
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) return Optional.of(mapear(rs));
			}
			
		} catch (SQLException e) {
			System.err.println("[ERRO]: Erro ao buscar aluno de nome " + nome + ": " + e);
			e.printStackTrace();
			throw new DatabaseException("Erro interno no banco dados");
		}
		
		return Optional.empty();
	}
	
	
	@Override
	public Aluno mapear(ResultSet rs) throws SQLException {
		return new Aluno(
			rs.getLong("id"),
			rs.getString("matricula"),
			rs.getString("nome"),
			rs.getString("email")
		);
	}
}
