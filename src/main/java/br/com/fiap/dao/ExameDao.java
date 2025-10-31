package br.com.fiap.dao;

import br.com.fiap.models.Exame;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para operações com exames no banco de dados
 * Responsável pelo CRUD de exames médicos
 */
@ApplicationScoped
public class ExameDao {

//    @Inject
//    Connection conexao;

    /**
     * Cadastra um novo exame no sistema
     * O ID é gerado automaticamente pelo banco de dados
     * @param exame Objeto Exame a ser cadastrado (sem ID)
     */
    @Inject
    private DataSource dataSource;

    @ApplicationScoped
    public void cadastrarExame(Exame exame) throws SQLException {
        Connection conexao = dataSource.getConnection();
        PreparedStatement comandoSQL = null;

        try {
            String sql = "INSERT INTO TBL_HC_EXAME(nome_exame, resultado_exame, status_resultado) " +
                    "VALUES(?, ?, ?)";

            comandoSQL = conexao.prepareStatement(sql, new String[]{"id_exame"});

            comandoSQL.setString(1, exame.getNome_exame());
            comandoSQL.setString(2, exame.getResultado_exame());
            comandoSQL.setString(3, exame.getStatus_resultado());

            comandoSQL.executeUpdate();

            // Recupera o ID gerado automaticamente pelo banco
            ResultSet generatedKeys = comandoSQL.getGeneratedKeys();
            if (generatedKeys.next()) {
                exame.setId_exame(generatedKeys.getInt(1));
            }

            comandoSQL.close();
            conexao.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao cadastrar exame: " + e.getMessage());
        }
    }

    /**
     * Lista todos os exames cadastrados no sistema
     * @return Lista de objetos Exame ordenados por ID
     */
    public List<Exame> listarExames() throws SQLException {
        List<Exame> exames = new ArrayList<>();
        Connection conexao = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conexao.prepareStatement("SELECT * FROM TBL_HC_EXAME ORDER BY id_exame");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Exame exame = new Exame();
                exame.setId_exame(rs.getInt("id_exame"));
                exame.setNome_exame(rs.getString("nome_exame"));
                exame.setResultado_exame(rs.getString("resultado_exame"));
                exame.setStatus_resultado(rs.getString("status_resultado"));
                exames.add(exame);
            }

            ps.close();
            conexao.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar exames: " + e.getMessage());
        }

        return exames;
    }

    /**
     * Lista exames com resultado e status formatados
     * @return Lista de strings com informações completas dos exames
     */
    public List<String> ListarExamesComResultado() throws SQLException {
        List<String> examesComStatus = new ArrayList<>();
        Connection conexao = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conexao.prepareStatement("SELECT * FROM TBL_HC_EXAME ORDER BY id_exame");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Exame exame = new Exame();
                exame.setId_exame(rs.getInt("id_exame"));
                exame.setNome_exame(rs.getString("nome_exame"));
                exame.setResultado_exame(rs.getString("resultado_exame"));
                exame.setStatus_resultado(rs.getString("status_resultado"));

                // Usa diretamente o status_resultado armazenado no banco
                String status = exame.getStatus_resultado();
                String descricao = "ID: " + exame.getId_exame() +
                        ", Nome: " + exame.getNome_exame() +
                        ", Resultado: " + exame.getResultado_exame() +
                        ", Status: " + status;
                examesComStatus.add(descricao);
            }

            ps.close();
            conexao.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar exames com resultado: " + e.getMessage());
        }

        return examesComStatus;
    }

    /**
     * Busca um exame específico pelo seu ID
     * @param id ID do exame a ser buscado
     * @return Objeto Exame encontrado ou null se não existir
     */
    public Exame buscarPorIdExame(int id) throws SQLException {
        System.out.println("Chamando buscarPorIdExame com ID: " + id);

        Connection conexao = dataSource.getConnection();
        PreparedStatement ps = null;
        Exame exame = null;

        try {
            String sql = "SELECT * FROM TBL_HC_EXAME WHERE id_exame = ?";
            ps = conexao.prepareStatement(sql);
            ps.setInt(1, id);
            System.out.println("Executando consulta SQL: " + sql + " com id_exame: " + id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Exame encontrado no banco com ID: " + id);
                exame = new Exame();
                exame.setId_exame(rs.getInt("id_exame"));
                exame.setNome_exame(rs.getString("nome_exame"));
                exame.setResultado_exame(rs.getString("resultado_exame"));
                exame.setStatus_resultado(rs.getString("status_resultado"));
            } else {
                System.out.println("Nenhum exame encontrado no banco com ID: " + id);
            }

            rs.close();
            ps.close();
            conexao.close();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar exame por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar exame por ID: " + e.getMessage());
        }
        System.out.println("Resultado do método buscarPorIdExame: " + exame);
        return exame;
    }

    /**
     * Atualiza os dados de um exame existente
     * @param exame Objeto Exame com os dados atualizados
     */
    public void updateExame(Exame exame) throws SQLException {
        Connection conexao = dataSource.getConnection();
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE TBL_HC_EXAME SET nome_exame = ?, resultado_exame = ?, status_resultado = ? WHERE id_exame = ?";
            ps = conexao.prepareStatement(sql);
            ps.setString(1, exame.getNome_exame());
            ps.setString(2, exame.getResultado_exame());
            ps.setString(3, exame.getStatus_resultado());
            ps.setInt(4, exame.getId_exame());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Exame não encontrado para atualização");
            }

            ps.close();
            conexao.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar exame: " + e.getMessage());
        }
    }
//ExameDao
    /**
     * Exclui um exame do sistema pelo ID
     * @param id ID do exame a ser excluído
     */
    public void excluirExame(int id) throws SQLException {
        Connection conexao = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conexao.prepareStatement("DELETE FROM TBL_HC_EXAME WHERE id_exame = ?");
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Exame não encontrado para exclusão");
            }

            ps.close();
            conexao.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir exame: " + e.getMessage());
        }
    }
}