package br.com.fiap.service;

import br.com.fiap.dao.ExameDao;
import br.com.fiap.dto.ExameRequestDto;
import br.com.fiap.dto.ExameResponseDto;
import br.com.fiap.models.Exame;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para exames
 */
@ApplicationScoped
public class ExameService {
    @Inject
    private ExameDao exameDao;

    /**
     * Lista todos os exames
     */
    public List<ExameResponseDto> listar() throws SQLException {
        List<Exame> exameList = exameDao.listarExames();
        return exameList.stream()
                .map(ExameResponseDto::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca exame por ID
     */
    public ExameResponseDto buscarPorId(int id) throws SQLException {
        Exame exame = exameDao.buscarPorIdExame(id);
        if (exame == null || exame.getId_exame() == 0) {
            throw new NotFoundException("Exame com ID " + id + " não encontrado");
        }
        return ExameResponseDto.convertToDto(exame);
    }

    /**
     * Cadastra novo exame
     */
    public Exame cadastrar(ExameRequestDto exameDto) throws SQLException {
        if (exameDto.getNome_exame() == null || exameDto.getNome_exame().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do exame é obrigatório para cadastro");
        }
        if (exameDto.getResultado_exame() == null || exameDto.getResultado_exame().trim().isEmpty()) {
            throw new IllegalArgumentException("Resultado do exame é obrigatório para cadastro");
        }
        if (exameDto.getStatus_resultado() == null || exameDto.getStatus_resultado().trim().isEmpty()) {
            throw new IllegalArgumentException("Status do resultado é obrigatório para cadastro");
        }

        Exame exame = new Exame();
        exame.setNome_exame(exameDto.getNome_exame().trim());
        exame.setResultado_exame(exameDto.getResultado_exame().trim());
        exame.setStatus_resultado(exameDto.getStatus_resultado().trim());

        exameDao.cadastrarExame(exame);
        return exame;
    }

    /**
     * Atualiza exame existente usando DTO
     */
    public void atualizar(ExameRequestDto exameDto, int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do exame deve ser positivo para atualização");
        }
        if (exameDto.getNome_exame() == null || exameDto.getNome_exame().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do exame é obrigatório para atualização");
        }
        if (exameDto.getResultado_exame() == null || exameDto.getResultado_exame().trim().isEmpty()) {
            throw new IllegalArgumentException("Resultado do exame é obrigatório para atualização");
        }
        if (exameDto.getStatus_resultado() == null || exameDto.getStatus_resultado().trim().isEmpty()) {
            throw new IllegalArgumentException("Status do resultado é obrigatório para atualização");
        }


        buscarPorId(id);

        Exame exame = new Exame();
        exame.setId_exame(id);
        exame.setNome_exame(exameDto.getNome_exame().trim());
        exame.setResultado_exame(exameDto.getResultado_exame().trim());
        exame.setStatus_resultado(exameDto.getStatus_resultado().trim());

        exameDao.updateExame(exame);
    }

    /**
     * Atualiza exame existente (sobrecarga)
     */
    public void atualizar(Exame exame) throws SQLException {
        if (exame.getId_exame() <= 0) {
            throw new IllegalArgumentException("ID do exame deve ser positivo para atualização");
        }
        if (exame.getNome_exame() == null || exame.getNome_exame().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do exame é obrigatório para atualização");
        }
        if (exame.getResultado_exame() == null || exame.getResultado_exame().trim().isEmpty()) {
            throw new IllegalArgumentException("Resultado do exame é obrigatório para atualização");
        }
        if (exame.getStatus_resultado() == null || exame.getStatus_resultado().trim().isEmpty()) {
            throw new IllegalArgumentException("Status do resultado é obrigatório para atualização");
        }

        exameDao.updateExame(exame);
    }

    /**
     * Exclui exame por ID
     */
    public void excluir(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do exame deve ser positivo para exclusão");
        }


        buscarPorId(id);

        exameDao.excluirExame(id);
    }

    /**
     * Lista exames com informações completas
     */
    public List<String> listarExamesComResultado() throws SQLException {
        return exameDao.ListarExamesComResultado();
    }
}