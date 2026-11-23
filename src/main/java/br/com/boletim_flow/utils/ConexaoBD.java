package br.com.boletim_flow.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.Console;
import org.h2.tools.RunScript;

public class ConexaoBD {
//	private static final String URL;
//	private static final String USER;
//	private static final String PASSWORD;
	
	public static void conectarBD() {
	}
	
	public static void inicializarDados(Connection conn) {
		try {
			boolean tabelaVazia = isTabelaVazia(conn, "disciplina");
			
			if(tabelaVazia) {
				System.out.println("[DEBUG]: Populando banco de dados...");
				InputStream in = ConexaoBD.class.getResourceAsStream("db/data.sql");
				
				if(in == null) {
                    System.err.println("data.sql não encontrado no classpath (/db/data.sql).");
                    return;
				}
				RunScript.execute(conn, new InputStreamReader(in, StandardCharsets.UTF_8));
				
				System.out.println("data.sql executado com sucesso!");
			} else {
				System.out.println("Dados iniciais já existem. data.sql não será executado.");
			}
			
		} catch(Exception e) {
            System.err.println("Erro ao executar data.sql: " + e.getMessage());
            e.printStackTrace();
		}
	
		
	}
	

    private static boolean isTabelaVazia(Connection conn, String nomeTabela) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + nomeTabela;

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                int qtd = rs.getInt(1);
                return qtd == 0;
            }
        }
        return true;
    }
    
    private static void iniciarH2Console() {
        Runnable consoleTask = () -> {
            System.out.println("\n[CONSOLE H2] Console Web sendo iniciado em uma Thread separada (http://localhost:8082)...");
            try {
                Console.main(new String[0]);
            } catch (Exception e) {
                System.err.println("[CONSOLE H2] Erro ao iniciar o Console Web.");
                e.printStackTrace();
            }
        };

        Thread consoleThread = new Thread(consoleTask);
        consoleThread.setName("H2-Console-Thread");
        consoleThread.start();
    }
}
