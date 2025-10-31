package br.com.fiap.service;

import br.com.fiap.dao.HistoricoConsultaDao;
import br.com.fiap.dto.HistoricoConsultaRequestDto;
import br.com.fiap.dto.HistoricoConsultaResponseDto;
import br.com.fiap.models.HistoricoConsulta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para operações com histórico de consultas
 */
@ApplicationScoped
public class HistoricoService {

    @Inject
    private HistoricoConsultaDao historicoDao;

    public List<HistoricoConsultaResponseDto> listar() {
        List<HistoricoConsulta> historicoConsultas = historicoDao.listarTodosHistoricos();
        return historicoConsultas.stream()
                .map(HistoricoConsultaResponseDto::convertToDto)
                .collect(Collectors.toList());
    }

    public HistoricoConsultaResponseDto buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do histórico deve ser positivo");
        }

        HistoricoConsulta historicoConsulta = historicoDao.buscarPorIdhistorico(id);
        if (historicoConsulta == null || !historicoConsulta.possuiId()) {
            throw new NotFoundException("Histórico de consulta com ID " + id + " não encontrado");
        }
        return HistoricoConsultaResponseDto.convertToDto(historicoConsulta);
    }

    public HistoricoConsultaResponseDto cadastrar(HistoricoConsultaRequestDto historicoDto) {
        if (!historicoDto.isValid()) {
            throw new IllegalArgumentException("Dados do histórico são inválidos");
        }

        HistoricoConsulta historico = new HistoricoConsulta();
        historico.setSintomasHistorico(historicoDto.getSintomasHistorico().trim());
        historico.setDiagnostico(historicoDto.getDiagnostico().trim());
        historico.setObservacao(historicoDto.getObservacao().trim());

        historicoDao.cadastrarHistoricoConsulta(historico);
        return buscarPorId(historico.getIdHistorico());
    }

    public void atualizar(HistoricoConsultaRequestDto historicoDto, int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do histórico deve ser positivo");
        }
        if (!historicoDto.isValid()) {
            throw new IllegalArgumentException("Dados do histórico são inválidos");
        }

        buscarPorId(id);

        HistoricoConsulta historico = new HistoricoConsulta();
        historico.setIdHistorico(id);
        historico.setSintomasHistorico(historicoDto.getSintomasHistorico().trim());
        historico.setDiagnostico(historicoDto.getDiagnostico().trim());
        historico.setObservacao(historicoDto.getObservacao().trim());

        historicoDao.upDateHistorico(historico);
    }

    public void excluir(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do histórico deve ser positivo");
        }

        buscarPorId(id);
        historicoDao.excluiHistoricoConsulta(id);
    }

    public List<HistoricoConsultaResponseDto> listarDiagnosticosCriticos() {
        List<HistoricoConsulta> historicos = historicoDao.listarTodosHistoricos();
        return historicos.stream()
                .filter(HistoricoConsulta::isDiagnosticoCritical)
                .map(HistoricoConsultaResponseDto::convertToDto)
                .collect(Collectors.toList());
    }
}