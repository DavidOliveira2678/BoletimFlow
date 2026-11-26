package br.com.boletim_flow.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.boletim_flow.entity.Aluno;
import br.com.boletim_flow.entity.Boletim;
import br.com.boletim_flow.entity.BoletimLinha;
import br.com.boletim_flow.entity.Nota;
import br.com.boletim_flow.exception.DatabaseException;
import br.com.boletim_flow.utils.ConexaoBD;

public class BoletimDAO {
	
	public Boletim montarBoletim(Aluno aluno){
		String query = "SELECT d.nome AS disciplina, n.bimestre, n.valor\n"
						+ "FROM nota n\n"
						+ "JOIN disciplina d ON d.id = n.disciplina_id\n"
						+ "WHERE n.aluno_id = ?\n"
						+ "ORDER BY d.nome, n.bimestre;";
		
		Map<String, List<BigDecimal>> notasPorDisciplina = new LinkedHashMap<>();
		
		try(Connection conn = ConexaoBD.getConnection();
			PreparedStatement ps = conn.prepareStatement(query)){
			
			ps.setLong(1, aluno.id());
			
			try(ResultSet rs = ps.executeQuery()){
				
				while(rs.next()) {
					notasPorDisciplina.computeIfAbsent(
							rs.getString("disciplina"),
							k -> new ArrayList<>()).add(rs.getBigDecimal("valor"));
				}
			}
		
		} catch (SQLException e) {
			System.err.println("[ERRO]: Erro ao gerar boletim do aluno: " + aluno.nome() + ": " + e);
			e.printStackTrace();
			throw new DatabaseException("Erro interno no banco dados");
		}
		
		List<BoletimLinha> linhas = new ArrayList<>();
		
		for (Map.Entry<String, List<BigDecimal>> entrada : notasPorDisciplina.entrySet()) {
			
			String disciplina = entrada.getKey();
			List<BigDecimal> notas = entrada.getValue();
			BoletimLinha boletimLinha = new BoletimLinha(disciplina, notas, calcularMedia(notas));
			linhas.add(boletimLinha);
		}
		
		return new Boletim(aluno, linhas);
	}
	

	private BigDecimal calcularMedia(List<BigDecimal> notas) {
		return notas.stream()
					.reduce(BigDecimal.ZERO, BigDecimal::add)
					.divide(BigDecimal.valueOf(notas.size()), 2, RoundingMode.HALF_UP);
	}

}
