package br.com.boletim_flow.entity;

import java.math.BigDecimal;
import java.util.List;

public record BoletimLinha(String disciplina, List<BigDecimal> notas, BigDecimal media) {
	
}
