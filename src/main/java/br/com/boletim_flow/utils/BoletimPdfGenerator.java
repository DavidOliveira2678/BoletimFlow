package br.com.boletim_flow.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import br.com.boletim_flow.entity.Boletim;
import br.com.boletim_flow.entity.BoletimLinha;
import br.com.boletim_flow.exception.PdfGenerationException;

public class BoletimPdfGenerator {
	public static Path gerarPdf(Boletim boletim, Path pastaDestino) {
		
		Document doc = new Document(PageSize.A4);
		Path arquivo = pastaDestino.resolve("boletim_" + boletim.aluno().matricula() + ".pdf");
		
		try {
			Files.createDirectories(pastaDestino);
			
			try (OutputStream out = Files.newOutputStream(arquivo)) {
				
				PdfWriter.getInstance(doc, out);
				doc.open();
				
				doc.add(new Paragraph("Boletim escolar"));
				doc.add(new Paragraph(
						"Nome do aluno: " + boletim.aluno().nome() + " | Matrícula do aluno: " + boletim.aluno().matricula()
						));
				
				PdfPTable tabela = new PdfPTable(6);
				tabela.setWidthPercentage(100);
				
				tabela.addCell("Disciplina");
				tabela.addCell("1º Bim");
				tabela.addCell("2º Bim");
				tabela.addCell("3º Bim");
				tabela.addCell("4º Bim");
				tabela.addCell("Média");
				tabela.setHeaderRows(1);
				
				for(BoletimLinha boletimLinha : boletim.linhas()) {
					tabela.addCell(boletimLinha.disciplina());
					
					for(BigDecimal nota : boletimLinha.notas()) {
						tabela.addCell(String.valueOf(nota));
					}
					
					tabela.addCell(String.valueOf(boletimLinha.media()));
				}
				
				doc.add(tabela);
				doc.close();
				return arquivo;
			}
			
		} catch(DocumentException | IOException e) {
			
			try {
				Files.deleteIfExists(arquivo);
			} catch (IOException ignorada) {
				e.addSuppressed(ignorada);
			}
			
			throw new PdfGenerationException("Erro ao gerar boletim do aluno " + boletim.aluno().nome(), e);
		}
	}
	
	private BoletimPdfGenerator() {}

}