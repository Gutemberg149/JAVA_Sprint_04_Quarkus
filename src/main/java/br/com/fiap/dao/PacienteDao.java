package br.com.fiap.dao;

import br.com.fiap.models.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@ApplicationScoped
public class PacienteDao {

    /**
     * Cadastra um novo paciente (ID gerado automaticamente) - VERSÃO FINAL CORRIGIDA
     */
    @Inject
    DataSource dataSource;

    public void cadastrarPaciente(Paciente paciente) {
        if (!paciente.isCpfValido()) {
            throw new IllegalArgumentException("CPF inválido: deve ter exatamente 11 dígitos");
        }

        Connection conexao = null;
        PreparedStatement comandoSQL = null;
        ResultSet generatedKeys = null;

        try {
            conexao = dataSource.getConnection();

            String[] generatedColumns = {"id_paciente"};
            comandoSQL = conexao.prepareStatement(
                    "INSERT INTO TBL_HC_PACIENTES(nome_paciente, cpf_paciente) VALUES (?, ?)",
                    generatedColumns);

            String cpf = paciente.getCpf();
            if (cpf == null || !cpf.matches("\\d{11}")) {
                throw new IllegalArgumentException("CPF deve conter exatamente 11 dígitos numéricos: " + cpf);
            }

            System.out.println("Cadastrando paciente: Nome=" + paciente.getNome() + ", CPF=" + cpf);

            comandoSQL.setString(1, paciente.getNome());
            comandoSQL.setString(2, cpf);

            int affectedRows = comandoSQL.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao cadastrar paciente, nenhuma linha afetada.");
            }

            generatedKeys = comandoSQL.getGeneratedKeys();
            if (generatedKeys != null && generatedKeys.next()) {
                try {

                    int generatedId = generatedKeys.getInt(1);
                    paciente.setId(generatedId);
                    System.out.println("Paciente cadastrado com sucesso. ID gerado: " + generatedId);
                } catch (SQLException e) {

                    try {
                        String generatedIdStr = generatedKeys.getString(1);
                        if (generatedIdStr != null) {
                            int generatedId = Integer.parseInt(generatedIdStr);
                            paciente.setId(generatedId);
                            System.out.println("Paciente cadastrado com sucesso. ID gerado (string): " + generatedId);
                        }
                    } catch (NumberFormatException nfe) {
                        System.err.println("Falha ao converter ID gerado: " + generatedKeys.getString(1));
                        throw new SQLException("Falha ao obter ID gerado para o paciente");
                    }
                }
            } else {
                throw new SQLException("Falha ao obter ID gerado para o paciente - nenhuma chave retornada.");
            }

        } catch (SQLException e) {
            System.out.println("Erro SQL ao cadastrar paciente: " + e.getMessage());
            throw new RuntimeException("Erro ao cadastrar paciente: " + paciente.getNome(), e);
        } finally {

            try {
                if (generatedKeys != null) generatedKeys.close();
                if (comandoSQL != null) comandoSQL.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    /**
     * Lista todos os pacientes (sem relação com ConsultaOnline)
     */
    public List<Paciente> listarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement("SELECT * FROM TBL_HC_PACIENTES ORDER BY nome_paciente");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setId(rs.getInt("id_paciente"));
                paciente.setNome(rs.getString("nome_paciente"));
                paciente.setCpf(rs.getString("cpf_paciente"));

                pacientes.add(paciente);
            }

            System.out.println("Listados " + pacientes.size() + " pacientes");

        } catch (SQLException e) {
            System.out.println("Erro ao listar pacientes: " + e.getMessage());
            throw new RuntimeException("Erro ao listar pacientes", e);
        }
        return pacientes;
    }

    /**
     * Busca paciente por ID (sem relação com ConsultaOnline)
     */
    public Paciente buscarPorIdPaciente(int id) {
        Paciente paciente = null;

        try (Connection conexao =dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement("SELECT * FROM TBL_HC_PACIENTES WHERE id_paciente = ?")) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    paciente = new Paciente();
                    paciente.setId(rs.getInt("id_paciente"));
                    paciente.setNome(rs.getString("nome_paciente"));
                    paciente.setCpf(rs.getString("cpf_paciente"));

                    System.out.println("Paciente encontrado: ID=" + id + ", Nome=" + paciente.getNome());
                } else {
                    System.out.println("Paciente não encontrado com ID: " + id);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar paciente por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar paciente por ID: " + id, e);
        }
        return paciente;
    }

    /**
     * Atualiza um paciente existente
     */
    public void updatePaciente(Paciente paciente) {
        if (paciente.getId() <= 0) {
            throw new IllegalArgumentException("ID do paciente deve ser positivo");
        }
        if (!paciente.isCpfValido()) {
            throw new IllegalArgumentException("CPF inválido: deve ter exatamente 11 dígitos");
        }

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(
                     "UPDATE TBL_HC_PACIENTES SET nome_paciente = ?, cpf_paciente = ? WHERE id_paciente = ?")) {


            String cpf = paciente.getCpf();
            if (cpf == null || !cpf.matches("\\d{11}")) {
                throw new IllegalArgumentException("CPF deve conter exatamente 11 dígitos numéricos: " + cpf);
            }

            ps.setString(1, paciente.getNome());
            ps.setString(2, cpf);
            ps.setInt(3, paciente.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Paciente atualizado com sucesso. ID: " + paciente.getId());
            } else {
                throw new RuntimeException("Nenhum paciente atualizado (ID não encontrado): " + paciente.getId());
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar paciente: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar paciente: " + paciente.getId(), e);
        }
    }

    /**
     * Exclui um paciente por ID
     */
    public void excluirPaciente(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do paciente deve ser positivo");
        }

        try (Connection conexao =dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement("DELETE FROM TBL_HC_PACIENTES WHERE id_paciente = ?")) {

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Paciente excluído com sucesso. ID: " + id);
            } else {
                throw new RuntimeException("Nenhum paciente excluído (ID não encontrado): " + id);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir paciente: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir paciente: " + id, e);
        }
    }

    /**
     * Busca paciente por CPF
     */
    public Paciente buscarPorCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty() || cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido");
        }

        if (!cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter apenas números: " + cpf);
        }

        Paciente paciente = null;

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement("SELECT * FROM TBL_HC_PACIENTES WHERE cpf_paciente = ?")) {


            ps.setString(1, cpf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    paciente = new Paciente();
                    paciente.setId(rs.getInt("id_paciente"));
                    paciente.setNome(rs.getString("nome_paciente"));
                    paciente.setCpf(rs.getString("cpf_paciente"));

                    System.out.println("Paciente encontrado por CPF: " + cpf);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar paciente por CPF: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar paciente por CPF: " + cpf, e);
        }
        return paciente;
    }
}

