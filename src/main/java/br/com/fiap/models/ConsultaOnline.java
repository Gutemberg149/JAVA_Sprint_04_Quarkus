package br.com.fiap.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de consulta online
 */
@XmlRootElement
public class ConsultaOnline {
    private Integer idConsulta;

//    @JsonFormat(pattern = "dd-MMM-yyyy", locale = "pt_BR")
//    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dataConsulta;
    private String status;
    private String link;
    private Exame exame;
    private Medico medico;
    private Paciente paciente;
    private Integer idPaciente;
    private Integer idMedico;
    private Integer idExame;

    @JsonIgnore
    private List<Exame> listaExame = new ArrayList<>();

    /**
     * Construtor padrão
     */
    public ConsultaOnline() {
    }

    /**
     * Checks if the consulta has a valid ID
     */
    public boolean possuiIdValido() {
        return idConsulta != null && idConsulta > 0;
    }

    /**
     * Representação em string do objeto
     */
    @Override
    public String toString() {
        return "ConsultaOnline{" +
                "idConsulta=" + idConsulta +
                ", dataConsulta=" + dataConsulta +
                ", status='" + status + '\'' +
                ", link='" + link + '\'' +
                ", exame=" + (exame != null ? exame.toString() : "null") +
                ", medico=" + (medico != null ? medico.toString() : "null") +
                ", paciente=" + (paciente != null ? paciente.toString() : "null") +
                ", idPaciente=" + idPaciente +
                ", idMedico=" + idMedico +
                ", idExame=" + idExame +
                '}';
    }

    /**
     * Obtém ID da consulta
     */
    public Integer getIdConsulta() {
        return idConsulta;
    }

    /**
     * Define ID da consulta
     */
    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    /**
     * Obtém data da consulta
     */
    @JsonFormat(pattern = "dd-MMM-yyyy", locale = "pt_BR")
    public LocalDate getDataConsulta() {
        return dataConsulta;
    }

    /**
     * Define data da consulta
     */
    public void setDataConsulta(LocalDate dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    /**
     * Obtém status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Obtém link
     */
    public String getLink() {
        return link;
    }

    /**
     * Define link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Obtém exame
     */
    public Exame getExame() {
        return exame;
    }

    /**
     * Define exame
     */
    public void setExame(Exame exame) {
        this.exame = exame;
    }

    /**
     * Obtém médico
     */
    public Medico getMedico() {
        return medico;
    }

    /**
     * Define médico
     */
    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    /**
     * Obtém paciente
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * Define paciente
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * Obtém ID do paciente
     */
    public Integer getIdPaciente() {
        return idPaciente;
    }

    /**
     * Define ID do paciente
     */
    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    /**
     * Obtém ID do médico
     */
    public Integer getIdMedico() {
        return idMedico;
    }

    /**
     * Define ID do médico
     */
    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    /**
     * Obtém ID do exame
     */
    public Integer getIdExame() {
        return idExame;
    }

    /**
     * Define ID do exame
     */
    public void setIdExame(Integer idExame) {
        this.idExame = idExame;
    }

    /**
     * Obtém lista de exames
     */
    @JsonIgnore
    public List<Exame> getListaExame() {
        return listaExame;
    }

    /**
     * Define lista de exames
     */
    public void setListaExame(List<Exame> listaExame) {
        this.listaExame = listaExame;
    }
}