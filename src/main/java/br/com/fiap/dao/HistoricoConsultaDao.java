package br.com.fiap.dao;

import br.com.fiap.models.HistoricoConsulta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com histórico de consultas
 */
@ApplicationScoped
public class HistoricoConsultaDao {

    /**
     * Cadastra um novo histórico de consulta
     */
    @Inject
    private DataSource dataSource;

    @ApplicationScoped
    public void cadastrarHistoricoConsulta(HistoricoConsulta historicoconsulta) {
        String sql = "INSERT INTO TBL_HC_HISTORICOS (sintomas_historico, diagnostico, observacoes) VALUES (?, ?, ?)";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement comandoSQL = conexao.prepareStatement(sql, new String[]{"id_historico"})) {

            comandoSQL.setString(1, historicoconsulta.getSintomasHistorico());
            comandoSQL.setString(2, historicoconsulta.getDiagnostico());
            comandoSQL.setString(3, historicoconsulta.getObservacao());

            int rowsAffected = comandoSQL.executeUpdate();

            ResultSet generatedKeys = comandoSQL.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idGerado = generatedKeys.getInt(1);
                historicoconsulta.setIdHistorico(idGerado);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar histórico", e);
        }
    }

    /**
     * Lista todos os históricos cadastrados no sistema
     */
    public List<HistoricoConsulta> listarTodosHistoricos() {
        List<HistoricoConsulta> historicos = new ArrayList<>();
        String sql = "SELECT id_historico, sintomas_historico, diagnostico, observacoes FROM TBL_HC_HISTORICOS ORDER BY id_historico";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HistoricoConsulta historicoconsulta = new HistoricoConsulta();
                historicoconsulta.setIdHistorico(rs.getInt("id_historico"));
                historicoconsulta.setSintomasHistorico(rs.getString("sintomas_historico"));
                historicoconsulta.setDiagnostico(rs.getString("diagnostico"));
                historicoconsulta.setObservacao(rs.getString("observacoes"));
                historicos.add(historicoconsulta);
            }
            return historicos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar históricos", e);
        }
    }

    /**
     * Busca histórico por ID
     */
    public HistoricoConsulta buscarPorIdhistorico(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser positivo");
        }

        String sql = "SELECT id_historico, sintomas_historico, diagnostico, observacoes FROM TBL_HC_HISTORICOS WHERE id_historico = ?";
        HistoricoConsulta historicoconsulta = null;

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    historicoconsulta = new HistoricoConsulta();
                    historicoconsulta.setIdHistorico(rs.getInt("id_historico"));
                    historicoconsulta.setSintomasHistorico(rs.getString("sintomas_historico"));
                    historicoconsulta.setDiagnostico(rs.getString("diagnostico"));
                    historicoconsulta.setObservacao(rs.getString("observacoes"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar histórico", e);
        }
        return historicoconsulta;
    }

    /**
     * Atualiza um histórico existente
     */
    public void upDateHistorico(HistoricoConsulta historicoconsulta) {
        if (historicoconsulta.getIdHistorico() <= 0) {
            throw new IllegalArgumentException("ID do histórico deve ser positivo");
        }

        String sql = "UPDATE TBL_HC_HISTORICOS SET sintomas_historico = ?, diagnostico = ?, observacoes = ? WHERE id_historico = ?";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, historicoconsulta.getSintomasHistorico());
            ps.setString(2, historicoconsulta.getDiagnostico());
            ps.setString(3, historicoconsulta.getObservacao());
            ps.setInt(4, historicoconsulta.getIdHistorico());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Histórico não encontrado para atualização");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar histórico", e);
        }
    }

    /**
     * Exclui um histórico por ID
     */
    public void excluiHistoricoConsulta(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do histórico deve ser positivo");
        }

        String sql = "DELETE FROM TBL_HC_HISTORICOS WHERE id_historico = ?";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Histórico não encontrado para exclusão");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir histórico", e);
        }
    }
}