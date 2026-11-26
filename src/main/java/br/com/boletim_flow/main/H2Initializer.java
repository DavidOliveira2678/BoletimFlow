package br.com.boletim_flow.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.Console;
import org.h2.tools.RunScript;

import br.com.boletim_flow.utils.ConexaoBD;

public class H2Initializer {

    public static void main(String[] args) {
        try (Connection conn = ConexaoBD.getConnection()) {
            System.out.println("[DEBUG]: Banco e schema inicializados.");
            inicializarDados(conn);
        } catch (SQLException | IOException e) {
            System.err.println("[ERRO]: Falha ao inicializar o banco.");
            e.printStackTrace();
            return;
        }

        iniciarH2Console();
        System.out.println("[DEBUG]: Aplicação pronta.");
    }

    private static void inicializarDados(Connection conn) throws SQLException, IOException {
        if (!isTabelaVazia(conn, "disciplina")) {
            System.out.println("[DEBUG]: Dados já existem. data.sql não será executado.");
            return;
        }

        System.out.println("[DEBUG]: Populando banco de dados...");
        try (InputStream in = H2Initializer.class.getResourceAsStream("/db/data.sql")) {
            if (in == null) {
                throw new IOException("data.sql não encontrado no classpath (/db/data.sql).");
            }
            RunScript.execute(conn, new InputStreamReader(in, StandardCharsets.UTF_8));
        }
        System.out.println("[DEBUG]: data.sql executado com sucesso!");
    }

    private static boolean isTabelaVazia(Connection conn, String tabela) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + tabela)) {
            return !rs.next() || rs.getInt(1) == 0;
        }
    }

    private static void iniciarH2Console() {
        Thread t = new Thread(() -> {
            System.out.println("[CONSOLE H2]: http://localhost:8082");
            try {
                Console.main(new String[0]);
            } catch (Exception e) {
                System.err.println("[CONSOLE H2]: Erro ao iniciar.");
                e.printStackTrace();
            }
        }, "H2-Console-Thread");
        t.start();
    }
}