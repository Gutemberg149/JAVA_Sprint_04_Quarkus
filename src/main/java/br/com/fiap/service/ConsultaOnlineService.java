package br.com.fiap.service;

import br.com.fiap.dao.ConsultaOnlineDao;
import br.com.fiap.dao.ExameDao;
import br.com.fiap.dao.MedicoDao;
import br.com.fiap.dao.PacienteDao;
import br.com.fiap.dto.ConsultaOnlineRequestDto;
import br.com.fiap.dto.ConsultaOnlineResponseDto;
import br.com.fiap.models.ConsultaOnline;
import br.com.fiap.models.Exame;
import br.com.fiap.models.Medico;
import br.com.fiap.models.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para consultas online
 */
@ApplicationScoped
public class ConsultaOnlineService {

    @Inject
    private ConsultaOnlineDao consultaOnlineDao;

    private ExameDao exameDao = new ExameDao();
    private MedicoDao medicoDao = new MedicoDao();
    private PacienteDao pacienteDao = new PacienteDao();

    /**
     * Lista todas as consultas online
     */
    public List<ConsultaOnlineResponseDto> listar() {
        List<ConsultaOnline> consultaOnlines = consultaOnlineDao.listarConsultasOnline();
        return consultaOnlines.stream()
                .map(ConsultaOnlineResponseDto::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca consulta online por ID
     */
    public ConsultaOnlineResponseDto buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID da consulta deve ser positivo");
        }

        ConsultaOnline consultaOnline = consultaOnlineDao.buscarPorIdConsultaOnline(id);
        if (consultaOnline == null) {
            throw new NotFoundException("Consulta online com ID " + id + " não encontrada");
        }
        return ConsultaOnlineResponseDto.convertToDto(consultaOnline);
    }

    /**
     * Cadastra nova consulta online - VERSÃO CORRIGIDA
     */
    public ConsultaOnline cadastrar(ConsultaOnlineRequestDto consultaDto) throws SQLException {
        validarDadosConsulta(consultaDto);

        ConsultaOnline consulta = new ConsultaOnline();
        consulta.setDataConsulta(consultaDto.getDataConsulta());
        consulta.setStatus(consultaDto.getStatus());
        consulta.setLink(consultaDto.getLink());


        if (consultaDto.getIdPaciente() == null || consultaDto.getIdPaciente() <= 0) {
            throw new IllegalArgumentException("ID do paciente é obrigatório");
        }
        Paciente paciente = pacienteDao.buscarPorIdPaciente(consultaDto.getIdPaciente());
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente com ID " + consultaDto.getIdPaciente() + " não encontrado");
        }
        consulta.setIdPaciente(consultaDto.getIdPaciente());
        consulta.setPaciente(paciente);


        if (consultaDto.getIdMedico() == null || consultaDto.getIdMedico() <= 0) {
            throw new IllegalArgumentException("ID do médico é obrigatório");
        }
        Medico medico = medicoDao.buscarPorIdMedico(consultaDto.getIdMedico());
        if (medico == null) {
            throw new IllegalArgumentException("Médico com ID " + consultaDto.getIdMedico() + " não encontrado");
        }
        consulta.setIdMedico(consultaDto.getIdMedico());
        consulta.setMedico(medico);


        if (consultaDto.getIdExame() != null && consultaDto.getIdExame() > 0) {
            Exame exame = exameDao.buscarPorIdExame(consultaDto.getIdExame());
            if (exame == null) {
                throw new IllegalArgumentException("Exame com ID " + consultaDto.getIdExame() + " não encontrado");
            }
            consulta.setIdExame(consultaDto.getIdExame());
            consulta.setExame(exame);
        }

        try {
            consultaOnlineDao.cadastrarConsultaOnline(consulta);
            return consulta;
        } catch (Exception e) {
            System.out.println("Erro detalhado ao cadastrar consulta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao cadastrar consulta online: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza consulta online existente - VERSÃO CORRIGIDA
     */
    public void atualizar(ConsultaOnlineRequestDto consultaDto, int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID da consulta deve ser positivo para atualização");
        }
        validarDadosConsulta(consultaDto);


        buscarPorId(id);

        ConsultaOnline consulta = new ConsultaOnline();
        consulta.setIdConsulta(id);
        consulta.setDataConsulta(consultaDto.getDataConsulta());
        consulta.setStatus(consultaDto.getStatus());
        consulta.setLink(consultaDto.getLink());

        if (consultaDto.getIdPaciente() == null || consultaDto.getIdPaciente() <= 0) {
            throw new IllegalArgumentException("ID do paciente é obrigatório");
        }
        Paciente paciente = pacienteDao.buscarPorIdPaciente(consultaDto.getIdPaciente());
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente com ID " + consultaDto.getIdPaciente() + " não encontrado");
        }
        consulta.setIdPaciente(consultaDto.getIdPaciente());
        consulta.setPaciente(paciente);


        if (consultaDto.getIdMedico() == null || consultaDto.getIdMedico() <= 0) {
            throw new IllegalArgumentException("ID do médico é obrigatório");
        }
        Medico medico = medicoDao.buscarPorIdMedico(consultaDto.getIdMedico());
        if (medico == null) {
            throw new IllegalArgumentException("Médico com ID " + consultaDto.getIdMedico() + " não encontrado");
        }
        consulta.setIdMedico(consultaDto.getIdMedico());
        consulta.setMedico(medico);


        if (consultaDto.getIdExame() != null && consultaDto.getIdExame() > 0) {
            Exame exame = exameDao.buscarPorIdExame(consultaDto.getIdExame());
            if (exame == null) {
                throw new IllegalArgumentException("Exame com ID " + consultaDto.getIdExame() + " não encontrado");
            }
            consulta.setIdExame(consultaDto.getIdExame());
            consulta.setExame(exame);
        } else {
            consulta.setIdExame(0);
            consulta.setExame(null);
        }

        try {
            consultaOnlineDao.updateConsultaOnline(consulta);
        } catch (Exception e) {
            System.out.println("Erro detalhado ao atualizar consulta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar consulta online: " + e.getMessage(), e);
        }
    }

    /**
     * Valida dados básicos da consulta
     */
    private void validarDadosConsulta(ConsultaOnlineRequestDto consultaDto) {
        if (consultaDto.getDataConsulta() == null) {
            throw new IllegalArgumentException("Data da consulta é obrigatória");
        }
        if (consultaDto.getStatus() == null || consultaDto.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Status é obrigatório");
        }
        if (consultaDto.getStatus().length() > 20) {
            throw new IllegalArgumentException("Status não pode exceder 20 caracteres");
        }
    }

    /**
     * Método depreciado para atualizar consulta
     */
    @Deprecated
    public void atualizar(ConsultaOnline consultaOnline) {
        if (consultaOnline.getIdConsulta() <= 0) {
            throw new IllegalArgumentException("ID da consulta deve ser positivo para atualização");
        }
        if (consultaOnline.getDataConsulta() == null) {
            throw new IllegalArgumentException("Data da consulta é obrigatória para atualização");
        }

        buscarPorId(consultaOnline.getIdConsulta());

        try {
            consultaOnlineDao.updateConsultaOnline(consultaOnline);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar consulta online", e);
        }
    }

    /**
     * Exclui consulta online por ID
     */
    public void excluir(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID da consulta deve ser positivo para exclusão");
        }

        buscarPorId(id);

        try {
            consultaOnlineDao.excluirConsultaOnline(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir consulta online", e);
        }
    }

    /**
     * Busca consultas por médico
     */
    public List<ConsultaOnlineResponseDto> buscarPorMedico(int idMedico) {
        if (idMedico <= 0) {
            throw new IllegalArgumentException("ID do médico deve ser positivo");
        }

        List<ConsultaOnline> consultas = consultaOnlineDao.buscarPorMedico(idMedico);
        return consultas.stream()
                .map(ConsultaOnlineResponseDto::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca consultas por paciente
     */
    public List<ConsultaOnlineResponseDto> buscarPorPaciente(int idPaciente) {
        if (idPaciente <= 0) {
            throw new IllegalArgumentException("ID do paciente deve ser positivo");
        }

        List<ConsultaOnline> consultas = consultaOnlineDao.buscarPorPaciente(idPaciente);
        return consultas.stream()
                .map(ConsultaOnlineResponseDto::convertToDto)
                .collect(Collectors.toList());
    }
}