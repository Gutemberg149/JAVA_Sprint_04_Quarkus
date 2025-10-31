package br.com.fiap.dto;

import br.com.fiap.models.HistoricoConsulta;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para resposta de histórico de consulta
 * Utilizado para transferir dados do histórico para o cliente
 * Inclui o ID gerado automaticamente pelo banco de dados
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoricoConsultaResponseDto {

    @JsonProperty("id_historico")
    private Integer idHistorico;

    @JsonProperty("sintomas_historico")
    private String sintomas_historico;

    private String diagnostico;
    private String observacao;

    @JsonProperty("status_diagnostico")
    private String statusDiagnostico;

    /**
     * Converte entidade HistoricoConsulta para DTO de resposta
     * @param historicoConsulta Entidade HistoricoConsulta a ser convertida
     * @return DTO populado com dados do histórico incluindo ID gerado automaticamente
     */
    public static HistoricoConsultaResponseDto convertToDto(HistoricoConsulta historicoConsulta) {
        if (historicoConsulta == null) {
            return null;
        }

        HistoricoConsultaResponseDto dto = new HistoricoConsultaResponseDto();

        dto.setIdHistorico(historicoConsulta.getIdHistorico());
        dto.setDiagnostico(historicoConsulta.getDiagnostico());
        dto.setSintomas_historico(historicoConsulta.getSintomasHistorico());
        dto.setObservacao(historicoConsulta.getObservacao());


        dto.setStatusDiagnostico(dto.verificarStatusDiagnostico());

        return dto;
    }

    /**
     * Obtém ID do histórico gerado automaticamente pelo banco
     * @return ID único do histórico
     */
    public Integer getIdHistorico() {
        return idHistorico;
    }

    /**
     * Define ID do histórico
     * @param idHistorico ID gerado automaticamente pelo banco de dados
     */
    public void setIdHistorico(Integer idHistorico) {
        this.idHistorico = idHistorico;
    }

    /**
     * Obtém sintomas descritos no histórico
     * @return Sintomas do paciente
     */
    public String getSintomas_historico() {
        return sintomas_historico;
    }

    /**
     * Define sintomas do histórico
     * @param sintomas_historico Sintomas descritos pelo paciente
     */
    public void setSintomas_historico(String sintomas_historico) {
        this.sintomas_historico = sintomas_historico;
    }

    /**
     * Obtém diagnóstico médico
     * @return Diagnóstico estabelecido pelo médico
     */
    public String getDiagnostico() {
        return diagnostico;
    }

    /**
     * Define diagnóstico médico
     * @param diagnostico Diagnóstico estabelecido pelo médico
     */
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    /**
     * Obtém observações adicionais
     * @return Observações e recomendações médicas
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * Define observações adicionais
     * @param observacao Observações e recomendações médicas
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * Obtém status do diagnóstico baseado na análise do conteúdo
     * @return "Diagnóstico crítico" se contém "grave", caso contrário "Diagnóstico normal"
     */
    public String getStatusDiagnostico() {
        return statusDiagnostico;
    }

    /**
     * Define status do diagnóstico
     * @param statusDiagnostico Status calculado do diagnóstico
     */
    public void setStatusDiagnostico(String statusDiagnostico) {
        this.statusDiagnostico = statusDiagnostico;
    }

    /**
     * Verifica o status do diagnóstico baseado no conteúdo
     * @return Status do diagnóstico ("Diagnóstico crítico" ou "Diagnóstico normal")
     */
    private String verificarStatusDiagnostico() {
        if (diagnostico != null && diagnostico.toLowerCase().contains("grave")) {
            return "Diagnóstico crítico";
        } else {
            return "Diagnóstico normal";
        }
    }

    /**
     * Representação em string do objeto para debugging
     * @return String formatada com todos os dados do histórico
     */
    @Override
    public String toString() {
        return "HistoricoConsultaResponseDto{" +
                "idHistorico=" + idHistorico +
                ", sintomas_historico='" + sintomas_historico + '\'' +
                ", diagnostico='" + diagnostico + '\'' +
                ", observacao='" + observacao + '\'' +
                ", statusDiagnostico='" + statusDiagnostico + '\'' +
                '}';
    }
}