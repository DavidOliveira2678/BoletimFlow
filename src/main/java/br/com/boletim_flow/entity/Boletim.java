package br.com.boletim_flow.entity;

import java.util.List;

public record Boletim(Aluno aluno, List<BoletimLinha> linhas) {
	
}
