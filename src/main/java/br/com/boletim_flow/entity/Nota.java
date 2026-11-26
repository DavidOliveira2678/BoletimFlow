package br.com.boletim_flow.entity;

import java.math.BigDecimal;

public record Nota(long id, long alunoId, long disciplinaId, int bimestre, BigDecimal valor) {
	
}
