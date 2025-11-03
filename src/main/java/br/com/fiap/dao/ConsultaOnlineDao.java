
package br.com.fiap.dao;

import br.com.fiap.models.ConsultaOnline;
import br.com.fiap.models.Exame;
import br.com.fiap.models.Medico;
import br.com.fiap.models.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com consultas online
 */
@ApplicationScoped
public class ConsultaOnlineDao {

    @Inject
    private MedicoDao medicoDao;

    @Inject
    private PacienteDao pacienteDao;

    @Inject
    private ExameDao exameDao;

    /**
     * Cadastra uma nova consulta online - VERSÃO FINAL
     */

    @Inject
    private DataSource dataSource;


    public void cadastrarConsultaOnline(ConsultaOnline consultaOnline) {
        String sql = "INSERT INTO TBL_HC_CONSULTA_ONLINE (DATA_CONSULTA, STATUS, " +
                "LINK, ID_PACIENTE, ID_MEDICO, ID_EXAME) VALUES (?, ?, ?, ?, ?, ?)";


        try (Connection conexao = dataSource.getConnection();
             PreparedStatement comandoSQL = conexao.prepareStatement(sql, new String[]{"ID_CONSULTA"})) {

            if (consultaOnline.getDataConsulta() == null) {
                throw new IllegalArgumentException("Data da consulta não pode ser nula");
            }

            comandoSQL.setDate(1, Date.valueOf(consultaOnline.getDataConsulta()));
            comandoSQL.setString(2, consultaOnline.getStatus());
            comandoSQL.setString(3, consultaOnline.getLink());

            // Usar os IDs da classe ConsultaOnline (que agora estão sendo preenchidos pelo Service)
            if (consultaOnline.getIdPaciente() > 0) {
                comandoSQL.setInt(4, consultaOnline.getIdPaciente());
            } else {
                throw new IllegalArgumentException("ID do paciente é obrigatório");
            }

            if (consultaOnline.getIdMedico() > 0) {
                comandoSQL.setInt(5, consultaOnline.getIdMedico());
            } else {
                throw new IllegalArgumentException("ID do médico é obrigatório");
            }

            // Exame opcional - Corrigido para evitar NPE ao desempacotar Integer
            if (consultaOnline.getIdExame() != null && consultaOnline.getIdExame() > 0) {
                comandoSQL.setInt(6, consultaOnline.getIdExame());
            } else {
                comandoSQL.setNull(6, Types.INTEGER);
            }

            int rowsAffected = comandoSQL.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = comandoSQL.getGeneratedKeys()) {
                    if (generatedKeys != null && generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        consultaOnline.setIdConsulta(generatedId);
                        System.out.println("Consulta online inserida com sucesso. ID: " + generatedId);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar consulta online: " + e.getMessage());
            throw new RuntimeException("Erro ao cadastrar consulta online", e);
        }
    }

    /**
     * Lista todas as consultas online com objetos relacionados
     */
    public List<ConsultaOnline> listarConsultasOnline() {
        List<ConsultaOnline> consultas = new ArrayList<>();
        String sql = "SELECT " +
                "co.ID_CONSULTA, co.DATA_CONSULTA, co.STATUS, co.LINK, " +
                "p.ID_PACIENTE, p.NOME_PACIENTE, p.CPF_PACIENTE, " +
                "m.ID_MEDICO, m.NOME AS NOME_MEDICO, m.ESPECIALIDADE, m.CRM, " +
                "e.ID_EXAME, e.NOME_EXAME, e.RESULTADO_EXAME " +
                "FROM TBL_HC_CONSULTA_ONLINE co " +
                "LEFT JOIN TBL_HC_PACIENTES p ON co.ID_PACIENTE = p.ID_PACIENTE " +
                "LEFT JOIN TBL_HC_MEDICOS m ON co.ID_MEDICO = m.ID_MEDICO " +
                "LEFT JOIN TBL_HC_EXAME e ON co.ID_EXAME = e.ID_EXAME " +
                "ORDER BY co.DATA_CONSULTA DESC";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ConsultaOnline consulta = criarConsultaFromResultSet(rs);
                consultas.add(consulta);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar consultas online: " + e.getMessage());
            throw new RuntimeException("Erro ao listar consultas online", e);
        }
        return consultas;
    }

    /**
     * Busca consulta online por ID
     */
    public ConsultaOnline buscarPorIdConsultaOnline(int id) {
        String sql = "SELECT " +
                "co.ID_CONSULTA, co.DATA_CONSULTA, co.STATUS, co.LINK, " +
                "p.ID_PACIENTE, p.NOME_PACIENTE, p.CPF_PACIENTE, " +
                "m.ID_MEDICO, m.NOME AS NOME_MEDICO, m.ESPECIALIDADE, m.CRM, " +
                "e.ID_EXAME, e.NOME_EXAME, e.RESULTADO_EXAME " +
                "FROM TBL_HC_CONSULTA_ONLINE co " +
                "LEFT JOIN TBL_HC_PACIENTES p ON co.ID_PACIENTE = p.ID_PACIENTE " +
                "LEFT JOIN TBL_HC_MEDICOS m ON co.ID_MEDICO = m.ID_MEDICO " +
                "LEFT JOIN TBL_HC_EXAME e ON co.ID_EXAME = e.ID_EXAME " +
                "WHERE co.ID_CONSULTA = ?";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return criarConsultaFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar consulta online por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar consulta online", e);
        }
        return null;
    }

    /**
     * Busca consultas por médico
     */
    public List<ConsultaOnline> buscarPorMedico(int idMedico) {
        List<ConsultaOnline> consultas = new ArrayList<>();
        String sql = "SELECT " +
                "co.ID_CONSULTA, co.DATA_CONSULTA, co.STATUS, co.LINK, " +
                "p.ID_PACIENTE, p.NOME_PACIENTE, p.CPF_PACIENTE, " +
                "m.ID_MEDICO, m.NOME AS NOME_MEDICO, m.ESPECIALIDADE, m.CRM, " +
                "e.ID_EXAME, e.NOME_EXAME, e.RESULTADO_EXAME " +
                "FROM TBL_HC_CONSULTA_ONLINE co " +
                "LEFT JOIN TBL_HC_PACIENTES p ON co.ID_PACIENTE = p.ID_PACIENTE " +
                "LEFT JOIN TBL_HC_MEDICOS m ON co.ID_MEDICO = m.ID_MEDICO " +
                "LEFT JOIN TBL_HC_EXAME e ON co.ID_EXAME = e.ID_EXAME " +
                "WHERE co.ID_MEDICO = ? " +
                "ORDER BY co.DATA_CONSULTA DESC";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idMedico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ConsultaOnline consulta = criarConsultaFromResultSet(rs);
                    consultas.add(consulta);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar consultas por médico: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar consultas por médico", e);
        }
        return consultas;
    }

    /**
     * Busca consultas por paciente
     */
    public List<ConsultaOnline> buscarPorPaciente(int idPaciente) {
        List<ConsultaOnline> consultas = new ArrayList<>();
        String sql = "SELECT " +
                "co.ID_CONSULTA, co.DATA_CONSULTA, co.STATUS, co.LINK, " +
                "p.ID_PACIENTE, p.NOME_PACIENTE, p.CPF_PACIENTE, " +
                "m.ID_MEDICO, m.NOME AS NOME_MEDICO, m.ESPECIALIDADE, m.CRM, " +
                "e.ID_EXAME, e.NOME_EXAME, e.RESULTADO_EXAME " +
                "FROM TBL_HC_CONSULTA_ONLINE co " +
                "LEFT JOIN TBL_HC_PACIENTES p ON co.ID_PACIENTE = p.ID_PACIENTE " +
                "LEFT JOIN TBL_HC_MEDICOS m ON co.ID_MEDICO = m.ID_MEDICO " +
                "LEFT JOIN TBL_HC_EXAME e ON co.ID_EXAME = e.ID_EXAME " +
                "WHERE co.ID_PACIENTE = ? " +
                "ORDER BY co.DATA_CONSULTA DESC";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ConsultaOnline consulta = criarConsultaFromResultSet(rs);
                    consultas.add(consulta);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar consultas por paciente: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar consultas por paciente", e);
        }
        return consultas;
    }

    /**
     * Cria objeto ConsultaOnline a partir do ResultSet
     */
    private ConsultaOnline criarConsultaFromResultSet(ResultSet rs) throws SQLException {
        ConsultaOnline consulta = new ConsultaOnline();

        // Dados básicos da consulta
        consulta.setIdConsulta(rs.getInt("ID_CONSULTA"));

        Date dataSql = rs.getDate("DATA_CONSULTA");
        if (dataSql != null) {
            consulta.setDataConsulta(dataSql.toLocalDate());
        }

        consulta.setStatus(rs.getString("STATUS"));
        consulta.setLink(rs.getString("LINK"));


        Paciente paciente = new Paciente();
        paciente.setId(rs.getInt("ID_PACIENTE"));
        paciente.setNome(rs.getString("NOME_PACIENTE"));
        paciente.setCpf(rs.getString("CPF_PACIENTE"));
        consulta.setPaciente(paciente);


        Medico medico = new Medico();
        medico.setIdMedico(rs.getInt("ID_MEDICO"));
        medico.setNome(rs.getString("NOME_MEDICO"));
        medico.setEspecialidade(rs.getString("ESPECIALIDADE"));
        medico.setCrm(rs.getInt("CRM"));
        consulta.setMedico(medico);


        int idExame = rs.getInt("ID_EXAME");
        if (!rs.wasNull() && idExame > 0) {
            Exame exame = new Exame();
            exame.setId_exame(idExame);
            exame.setNome_exame(rs.getString("NOME_EXAME"));
            exame.setResultado_exame(rs.getString("RESULTADO_EXAME"));
            consulta.setExame(exame);
        }

        return consulta;
    }

    /**
     * Atualiza uma consulta online existente - VERSÃO COMPATÍVEL
     */
    public void updateConsultaOnline(ConsultaOnline consultaOnline) {
        if (consultaOnline.getIdConsulta() <= 0) {
            throw new IllegalArgumentException("ID da consulta deve ser positivo");
        }
        if (consultaOnline.getDataConsulta() == null) {
            throw new IllegalArgumentException("Data da consulta não pode ser nula");
        }

        String sql = "UPDATE TBL_HC_CONSULTA_ONLINE SET DATA_CONSULTA = ?, STATUS = ?, LINK = ?, ID_PACIENTE = ?, ID_MEDICO = ?, ID_EXAME = ? WHERE ID_CONSULTA = ?";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(consultaOnline.getDataConsulta()));
            ps.setString(2, consultaOnline.getStatus());
            ps.setString(3, consultaOnline.getLink());

            // Usar IDs diretamente (obrigatórios)
            if (consultaOnline.getIdPaciente() > 0) {
                ps.setInt(4, consultaOnline.getIdPaciente());
            } else {
                throw new IllegalArgumentException("ID do paciente é obrigatório");
            }

            if (consultaOnline.getIdMedico() > 0) {
                ps.setInt(5, consultaOnline.getIdMedico());
            } else {
                throw new IllegalArgumentException("ID do médico é obrigatório");
            }

            // Exame opcional - Corrigido para evitar NPE ao desempacotar Integer
            if (consultaOnline.getIdExame() != null && consultaOnline.getIdExame() > 0) {
                ps.setInt(6, consultaOnline.getIdExame());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.setInt(7, consultaOnline.getIdConsulta());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Consulta não encontrada para atualização");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar consulta online: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar consulta online", e);
        }
    }

    /**
     * Exclui uma consulta online por ID
     */
    public void excluirConsultaOnline(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID da consulta deve ser positivo");
        }

        String sql = "DELETE FROM TBL_HC_CONSULTA_ONLINE WHERE ID_CONSULTA = ?";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Consulta não encontrada para exclusão");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir consulta online: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir consulta online", e);
        }
    }
}
