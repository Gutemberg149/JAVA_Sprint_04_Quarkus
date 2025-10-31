package br.com.fiap.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para requisição de histórico de consulta
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoricoConsultaRequestDto {

    @JsonProperty("id_historico")
    private Integer idHistorico;

    @JsonProperty("sintomas_historico")
    private String sintomasHistorico;

    private String diagnostico;
    private String observacao;

    public HistoricoConsultaRequestDto() {}

    public HistoricoConsultaRequestDto(String sintomasHistorico, String diagnostico, String observacao) {
        this.sintomasHistorico = sintomasHistorico;
        this.diagnostico = diagnostico;
        this.observacao = observacao;
    }

    public boolean possuiId() {
        return idHistorico != null;
    }

    public Integer getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(Integer idHistorico) {
        this.idHistorico = idHistorico;
    }

    public String getSintomasHistorico() {
        return sintomasHistorico;
    }

    public void setSintomasHistorico(String sintomasHistorico) {
        this.sintomasHistorico = sintomasHistorico;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public boolean isValid() {
        return sintomasHistorico != null && !sintomasHistorico.trim().isEmpty() &&
                diagnostico != null && !diagnostico.trim().isEmpty() &&
                observacao != null && !observacao.trim().isEmpty();
    }
}