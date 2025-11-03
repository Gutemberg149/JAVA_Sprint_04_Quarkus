package br.com.fiap.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * DTO para requisição de consulta online
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaOnlineRequestDto {



    @JsonFormat(pattern = "dd-MMM-yyyy", locale = "pt_BR")
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

    public ConsultaOnlineRequestDto() {}

    public ConsultaOnlineRequestDto(LocalDate dataConsulta, String status, String link,
                                    Integer idExame, Integer idPaciente, Integer idMedico) {
        this.dataConsulta = dataConsulta;
        this.status = status;
        this.link = link;
        this.idExame = idExame;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
    }

    public LocalDate getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDate dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getIdExame() {
        return idExame;
    }

    public void setIdExame(Integer idExame) {
        this.idExame = idExame;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Integer getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public boolean isValidForCreate() {
        return dataConsulta != null &&
                status != null && !status.trim().isEmpty() &&
                link != null && !link.trim().isEmpty() &&
                idPaciente != null && idPaciente > 0 &&
                idMedico != null && idMedico > 0;
    }

    public boolean isValidForUpdate() {
        return idConsulta != null && idConsulta > 0 && isValidForCreate();
    }

    @Override
    public String toString() {
        return "ConsultaOnlineRequestDto{" +
                "dataConsulta=" + dataConsulta +
                ", status='" + status + '\'' +
                ", link='" + link + '\'' +
                ", idExame=" + idExame +
                ", idPaciente=" + idPaciente +
                ", idMedico=" + idMedico +
                ", idConsulta=" + idConsulta +
                '}';
    }

    public void validateForCreate() {
        if (dataConsulta == null) {
            throw new IllegalArgumentException("Data da consulta é obrigatória");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status é obrigatório");
        }
        if (link == null || link.trim().isEmpty()) {
            throw new IllegalArgumentException("Link é obrigatório");
        }
        if (idPaciente == null || idPaciente <= 0) {
            throw new IllegalArgumentException("ID do paciente é obrigatório e deve ser positivo");
        }
        if (idMedico == null || idMedico <= 0) {
            throw new IllegalArgumentException("ID do médico é obrigatório e deve ser positivo");
        }
    }

    public void validateForUpdate() {
        if (idConsulta == null || idConsulta <= 0) {
            throw new IllegalArgumentException("ID da consulta é obrigatório para atualização");
        }
        validateForCreate();
    }
}