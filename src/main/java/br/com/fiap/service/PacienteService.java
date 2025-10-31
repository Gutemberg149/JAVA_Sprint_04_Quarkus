package br.com.fiap.service;

import br.com.fiap.dao.PacienteDao;
import br.com.fiap.dto.PacienteRequestDto;
import br.com.fiap.dto.PacienteResponseDto;
import br.com.fiap.models.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para pacientes (sem relação com ConsultaOnline)
 */
@ApplicationScoped
public class PacienteService {
    @Inject
    private PacienteDao pacienteDao;

    /**
     * Lista todos os pacientes
     */
    public List<PacienteResponseDto> listar() {
        try {
            List<Paciente> pacienteList = pacienteDao.listarPacientes();
            List<PacienteResponseDto> response = pacienteList.stream()
                    .map(PacienteResponseDto::convertToDto)
                    .collect(Collectors.toList());

            System.out.println("Listados " + response.size() + " pacientes");
            return response;

        } catch (Exception e) {
            System.err.println("Erro ao listar pacientes: " + e.getMessage());
            throw new RuntimeException("Erro ao listar pacientes", e);
        }
    }

    /**
     * Busca paciente por ID
     */
    public PacienteResponseDto buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do paciente deve ser positivo");
        }

        try {
            Paciente paciente = pacienteDao.buscarPorIdPaciente(id);
            if (paciente == null || paciente.getId() == null) {
                throw new NotFoundException("Paciente com ID " + id + " não encontrado");
            }

            PacienteResponseDto response = PacienteResponseDto.convertToDto(paciente);
            System.out.println("Paciente encontrado: ID=" + id);
            return response;

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao buscar paciente ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Erro ao buscar paciente", e);
        }
    }

    /**
     * Busca paciente por CPF
     */
    public PacienteResponseDto buscarPorCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter exatamente 11 dígitos");
        }

        try {
            Paciente paciente = pacienteDao.buscarPorCpf(cpf);
            if (paciente == null || paciente.getId() == null) {
                return null;
            }

            PacienteResponseDto response = PacienteResponseDto.convertToDto(paciente);
            System.out.println("Paciente encontrado por CPF: " + cpf);
            return response;

        } catch (Exception e) {
            System.err.println("Erro ao buscar paciente CPF " + cpf + ": " + e.getMessage());
            throw new RuntimeException("Erro ao buscar paciente por CPF", e);
        }
    }

    /**
     * Cadastra novo paciente (ID gerado automaticamente) - VERSÃO CORRIGIDA
     */
    public PacienteResponseDto cadastrar(PacienteRequestDto pacienteDto) {

        if (pacienteDto == null) {
            throw new IllegalArgumentException("Dados do paciente não podem ser nulos");
        }


        pacienteDto.cleanData();
        if (!pacienteDto.isValid()) {
            throw new IllegalArgumentException("Dados do paciente inválidos ou incompletos");
        }

        try {

            Paciente pacienteExistente = pacienteDao.buscarPorCpf(pacienteDto.getCpfPaciente());
            if (pacienteExistente != null) {
                throw new IllegalArgumentException("CPF já cadastrado: " + pacienteDto.getCpfPaciente());
            }


            Paciente paciente = new Paciente();
            paciente.setNome(pacienteDto.getNomePaciente());
            paciente.setCpf(pacienteDto.getCpfPaciente());

            System.out.println("Tentando cadastrar paciente: " + paciente.getNome() + ", CPF: " + paciente.getCpf());


            pacienteDao.cadastrarPaciente(paciente);


            if (paciente.getId() == null) {
                throw new RuntimeException("Falha ao obter ID gerado para o paciente");
            }

            PacienteResponseDto response = PacienteResponseDto.convertToDto(paciente);
            System.out.println("Paciente cadastrado com sucesso: ID=" + response.getIdPaciente());
            return response;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro detalhado ao cadastrar paciente: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao cadastrar paciente: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza paciente existente
     */
    public void atualizar(PacienteRequestDto pacienteDto) {

        if (pacienteDto == null) {
            throw new IllegalArgumentException("Dados do paciente não podem ser nulos");
        }
        if (pacienteDto.getIdPaciente() == null || pacienteDto.getIdPaciente() <= 0) {
            throw new IllegalArgumentException("ID do paciente deve ser positivo para atualização");
        }


        pacienteDto.cleanData();
        if (!pacienteDto.isValid()) {
            throw new IllegalArgumentException("Dados do paciente inválidos ou incompletos");
        }

        try {

            Paciente pacienteExistente = pacienteDao.buscarPorIdPaciente(pacienteDto.getIdPaciente());
            if (pacienteExistente == null || pacienteExistente.getId() == null) {
                throw new NotFoundException("Paciente com ID " + pacienteDto.getIdPaciente() + " não encontrado para atualização");
            }


            if (!pacienteExistente.getCpf().equals(pacienteDto.getCpfPaciente())) {
                Paciente pacienteComCpf = pacienteDao.buscarPorCpf(pacienteDto.getCpfPaciente());
                if (pacienteComCpf != null && !pacienteComCpf.getId().equals(pacienteDto.getIdPaciente())) {
                    throw new IllegalArgumentException("CPF já cadastrado para outro paciente: " + pacienteDto.getCpfPaciente());
                }
            }


            Paciente paciente = new Paciente();
            paciente.setId(pacienteDto.getIdPaciente());
            paciente.setNome(pacienteDto.getNomePaciente());
            paciente.setCpf(pacienteDto.getCpfPaciente());


            pacienteDao.updatePaciente(paciente);
            System.out.println("Paciente atualizado com sucesso: ID=" + pacienteDto.getIdPaciente());

        } catch (NotFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar paciente ID " + pacienteDto.getIdPaciente() + ": " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar paciente", e);
        }
    }

    /**
     * Exclui paciente por ID
     */
    public void excluir(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do paciente deve ser positivo para exclusão");
        }

        try {

            Paciente paciente = pacienteDao.buscarPorIdPaciente(id);
            if (paciente == null || paciente.getId() == null) {
                throw new NotFoundException("Paciente com ID " + id + " não encontrado para exclusão");
            }


            pacienteDao.excluirPaciente(id);
            System.out.println("Paciente excluído com sucesso: ID=" + id);

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao excluir paciente ID " + id + ": " + e.getMessage());

            if (e.getMessage().contains("constraint") || e.getMessage().contains("foreign key")) {
                throw new RuntimeException("Não é possível excluir paciente: existem registros relacionados", e);
            }

            throw new RuntimeException("Erro ao excluir paciente", e);
        }
    }
}