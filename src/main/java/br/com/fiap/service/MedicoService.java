package br.com.fiap.service;

import br.com.fiap.dao.MedicoDao;
import br.com.fiap.dto.MedicoRequestDto;
import br.com.fiap.dto.MedicoResponseDto;
import br.com.fiap.models.Medico;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para médicos
 */
@ApplicationScoped
public class MedicoService {
    @Inject
    private MedicoDao medicoDao;

    /**
     * Lista todos os médicos
     */
    public List<MedicoResponseDto> listar() throws SQLException {
        List<Medico> medicoList = medicoDao.listarMedicos();
        return medicoList.stream()
                .map(MedicoResponseDto::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca médico por ID
     */
    public MedicoResponseDto buscarPorId(int id) throws SQLException {
        Medico medico = medicoDao.buscarPorIdMedico(id);
        if (medico == null) {
            throw new NotFoundException("Médico com ID " + id + " não encontrado");
        }
        return MedicoResponseDto.convertToDto(medico);
    }

    /**
     * Cadastra novo médico
     */
    public Medico cadastrar(MedicoRequestDto medicoDto) throws SQLException {
        if (medicoDto.getNome() == null || medicoDto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório para cadastro de médico");
        }
        if (medicoDto.getEspecialidade() == null || medicoDto.getEspecialidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Especialidade é obrigatória para cadastro de médico");
        }

        Medico medico = new Medico();

        medico.setNome(medicoDto.getNome());
        medico.setEspecialidade(medicoDto.getEspecialidade());
        medico.setCrm(medicoDto.getCrm());


        medicoDao.cadastrarMedico(medico);


        return medico;
    }

    /**
     * Atualiza médico existente
     */
    public void atualizar(MedicoRequestDto medicoDto) throws SQLException {
        if (medicoDto.getId_medico() == null || medicoDto.getId_medico() <= 0) {
            throw new IllegalArgumentException("ID do médico deve ser positivo para atualização");
        }
        if (medicoDto.getNome() == null || medicoDto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório para atualização");
        }
        if (medicoDto.getEspecialidade() == null || medicoDto.getEspecialidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Especialidade é obrigatória para atualização");
        }


        Medico existingMedico = medicoDao.buscarPorIdMedico(medicoDto.getId_medico());
        if (existingMedico == null) {
            throw new NotFoundException("Médico com ID " + medicoDto.getId_medico() + " não encontrado para atualização");
        }


        Medico medico = new Medico();
        medico.setIdMedico(medicoDto.getId_medico());
        medico.setNome(medicoDto.getNome());
        medico.setEspecialidade(medicoDto.getEspecialidade());
        medico.setCrm(medicoDto.getCrm());


        medicoDao.updateMedico(medico);
    }

    /**
     * Exclui médico por ID
     */
    public void excluir(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do médico deve ser positivo para exclusão");
        }

        medicoDao.excluirMedico(id);
    }

}