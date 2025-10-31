package br.com.fiap.dto;

//import br.com.fiap.CustomeDeserialize.CustomLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;

/**
 * DTO para requisição de consulta online
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaOnlineRequestDto {

    @JsonFormat(pattern = "dd-MMM-yyyy", locale = "pt_BR")
//    @JsonDeserialize(using = CustomLocalDateDeserializer)
    private LocalDate dataConsulta;

    private String status;
    private String link;

    @JsonProperty("id_exame")
    private Integer idExame;

    @JsonProperty("id_paciente")
    private Integer idPaciente;

    @JsonProperty("id_medico")
    private Integer idMedico;

    @JsonProperty("id_consulta")
    private Integer idConsulta;

    /**
     * Construtor padrão
     */
    public ConsultaOnlineRequestDto() {}

    /**
     * Obtém data da consulta
     */
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
}

