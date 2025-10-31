package br.com.fiap.dao;

import br.com.fiap.models.Medico;
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
 * DAO para operações com médicos
 */
@ApplicationScoped
public class MedicoDao {

    /**
     * Cadastra um novo médico
     */
    /**
     * Cadastra um novo médico
     */
    @Inject
    DataSource dataSource;

    @ApplicationScoped
    public void cadastrarMedico(Medico medico) throws SQLException {
        Connection conexao = dataSource.getConnection();
        if (conexao == null) {
            throw new RuntimeException("Falha ao obter conexão no cadastrarMedico");
        }
        PreparedStatement comandoSQL = null;
        ResultSet generatedKeys = null;

        try {
            // Remove id_medico da lista de colunas - o banco gera automaticamente
            String sql = "INSERT INTO TBL_HC_MEDICOS (nome, especialidade, crm) VALUES (?, ?, ?)";

            // Use RETURN_GENERATED_KEYS para obter o ID gerado
            comandoSQL = conexao.prepareStatement(sql, new String[]{"id_medico"});

            // Agora só temos 3 parâmetros (nome, especialidade, crm)
            comandoSQL.setString(1, medico.getNome());
            comandoSQL.setString(2, medico.getEspecialidade());
            comandoSQL.setInt(3, medico.getCrm());

            int affectedRows = comandoSQL.executeUpdate();

            // Recupera o ID gerado automaticamente
            if (affectedRows > 0) {
                generatedKeys = comandoSQL.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    medico.setIdMedico(generatedId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar médico: " + e.getMessage(), e);
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (comandoSQL != null) comandoSQL.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Lista todos os médicos
     */
    public List<Medico> listarMedicos() throws SQLException {
        List<Medico> medicos = new ArrayList<>();
        Connection conexao = dataSource.getConnection();
        if (conexao == null) {
            throw new RuntimeException("Falha ao obter conexão no listarMedicos");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT id_medico, nome, especialidade, crm FROM TBL_HC_MEDICOS ORDER BY id_medico";

            ps = conexao.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Medico medico = new Medico();
                medico.setIdMedico(rs.getInt("id_medico"));
                medico.setNome(rs.getString("nome"));
                medico.setEspecialidade(rs.getString("especialidade"));
                medico.setCrm(rs.getInt("crm"));

                medicos.add(medico);
            }
            System.out.println("DAO.listarMedicos: Retornou " + medicos.size() + " médicos");
        } catch (SQLException e) {
            System.err.println("SQLException no listarMedicos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar médicos: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return medicos;
    }

    /**
     * Atualiza um médico existente
     */
    public void updateMedico(Medico medico) throws SQLException {
        Connection conexao = dataSource.getConnection();
        if (conexao == null) {
            throw new RuntimeException("Falha ao obter conexão no updateMedico");
        }
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE TBL_HC_MEDICOS SET nome = ?, especialidade = ?, crm = ? WHERE id_medico = ?";
            ps = conexao.prepareStatement(sql);
            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getEspecialidade());
            ps.setInt(3, medico.getCrm());
            ps.setInt(4, medico.getIdMedico());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Nenhum médico atualizado (ID não encontrado): " + medico.getIdMedico());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar médico: " + medico.getIdMedico(), e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Exclui um médico por ID
     */
    public void excluirMedico(int id) throws SQLException {
        Connection conexao = dataSource.getConnection();
        if (conexao == null) {
            throw new RuntimeException("Falha ao obter conexão no excluirMedico");
        }
        PreparedStatement ps = null;
        try {
            ps = conexao.prepareStatement("DELETE FROM TBL_HC_MEDICOS WHERE id_medico = ?");
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Nenhum médico excluído (ID não encontrado): " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir médico: " + id, e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Busca médico por ID
     */
    public Medico buscarPorIdMedico(int id) throws SQLException {
        Connection conexao = dataSource.getConnection();
        if (conexao == null) {
            throw new RuntimeException("Falha ao obter conexão no buscarPorIdMedico");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        Medico medico = null;

        try {
            String sql = "SELECT id_medico, nome, especialidade, crm FROM TBL_HC_MEDICOS WHERE id_medico = ?";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                medico = new Medico();
                medico.setIdMedico(rs.getInt("id_medico"));
                medico.setNome(rs.getString("nome"));
                medico.setEspecialidade(rs.getString("especialidade"));
                medico.setCrm(rs.getInt("crm"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar médico por ID: " + id, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return medico;
    }

    /**
     * Busca médicos por especialidade
     */
    public List<Medico> buscarPorEspecialidade(String especialidade) throws SQLException {
        List<Medico> medicos = new ArrayList<>();
        Connection conexao = dataSource.getConnection();
        if (conexao == null) {
            throw new RuntimeException("Falha ao obter conexão no buscarPorEspecialidade");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT id_medico, nome, especialidade, crm FROM TBL_HC_MEDICOS WHERE especialidade LIKE ? ORDER BY nome";
            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + especialidade + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Medico medico = new Medico();
                medico.setIdMedico(rs.getInt("id_medico"));
                medico.setNome(rs.getString("nome"));
                medico.setEspecialidade(rs.getString("especialidade"));
                medico.setCrm(rs.getInt("crm"));
                medicos.add(medico);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar médicos por especialidade: " + especialidade, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return medicos;
    }
}