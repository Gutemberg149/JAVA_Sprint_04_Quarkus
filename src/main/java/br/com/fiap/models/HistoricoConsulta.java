package br.com.fiap.models;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Modelo de entidade HistoricoConsulta
 */
@XmlRootElement
public class HistoricoConsulta {
    private Integer idHistorico;
    private String sintomasHistorico;
    private String diagnostico;
    private String observacao;

    public HistoricoConsulta() {
    }

    public HistoricoConsulta(String sintomasHistorico, String diagnostico, String observacao) {
        this.sintomasHistorico = sintomasHistorico;
        this.diagnostico = diagnostico;
        this.observacao = observacao;
    }

    public boolean possuiId() {
        return idHistorico != null;
    }

    public boolean isDiagnosticoCritical() {
        return diagnostico != null && diagnostico.toLowerCase().contains("grave");
    }

    public String getStatusDiagnostico() {
        return isDiagnosticoCritical() ? "Crítico" : "Normal";
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