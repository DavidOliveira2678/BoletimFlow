package br.com.boletim_flow.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.boletim_flow.entity.Aluno;
import br.com.boletim_flow.entity.Nota;
import br.com.boletim_flow.exception.DatabaseException;
import br.com.boletim_flow.utils.ConexaoBD;

public class NotaDAO implements GenericDAO<Nota> {

	@Override
	public List<Nota> buscarTodos() {
		String query = "SELECT * FROM nota;";
		List<Nota> notas = new ArrayList<>();
		
		try(Connection conn = ConexaoBD.getConnection();
			PreparedStatement ps = conn.prepareStatement(query)){
			
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					Nota nota = mapear(rs);
					notas.add(nota);
				}
			}
			
		} catch (SQLException e) {
			System.err.println("[ERRO]: Erro ao buscar todos os alunos: " + e);
			e.printStackTrace();
			throw new DatabaseException("Erro interno no banco dados");
		}
		
		return notas;
	}
	

	@Override
	public Optional<Nota> buscarPorId(long id) {
		String query = "SELECT * FROM nota WHERE id = ?";
		
		try(Connection conn = ConexaoBD.getConnection();
			PreparedStatement ps = conn.prepareStatement(query)){
			
			ps.setLong(1, id);
			
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) return Optional.of(mapear(rs));
			}
			
		} catch (SQLException e) {
			System.err.println("[ERRO]: Erro ao buscar nota de ID " + id + ": " + e);
			e.printStackTrace();
			throw new DatabaseException("Erro interno no banco dados");
		}
		
		return Optional.empty();
	}
	
	
	private Nota mapear(ResultSet rs) throws SQLException {
		return new Nota(rs.getLong("id"),
				rs.getLong("aluno_id"),
				rs.getLong("disciplina_id"),
				rs.getInt("bimestre"),
				rs.getBigDecimal("valor")
		);
	}

}